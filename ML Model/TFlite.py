import pandas as pd
import numpy as np
import tensorflow as tf
from tensorflow import keras
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics.pairwise import cosine_similarity
from tensorflow.keras.optimizers import Adam

# Assuming 'data_nutrisi' and 'data' are already defined

# Fill missing values with 0 or appropriate values
data_nutrisi = data_nutrisi.fillna(0)

scaler = MinMaxScaler()
recipe_data_scaled = scaler.fit_transform(data_nutrisi)

# Split the dataset into training and validation sets
train_data, val_data = train_test_split(recipe_data_scaled, test_size=0.3, random_state=42)

# Build and compile the recommendation model
input_dim = recipe_data_scaled.shape[1]

model = tf.keras.models.Sequential([
    tf.keras.layers.InputLayer(input_shape=(input_dim,)),
    tf.keras.layers.Reshape((input_dim, 1)),  # Reshape for Conv1D
    tf.keras.layers.Conv1D(filters=32, kernel_size=5, strides=1, padding='causal', activation='relu'),
    tf.keras.layers.LSTM(64, return_sequences=True),
    tf.keras.layers.LSTM(64, return_sequences=True),
    tf.keras.layers.Flatten(),  # Flatten before Dense layers
    tf.keras.layers.Dense(30, activation='relu'),
    tf.keras.layers.Dense(10, activation='relu'),
    tf.keras.layers.Dense(1),
])

model.compile(loss='mean_squared_error', optimizer=Adam())

# Train the model
history = model.fit(train_data, train_data, epochs=20, batch_size=32, validation_data=(val_data, val_data))

# Convert the model to TensorFlow Lite
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# Save the TFLite model to a file
tflite_model_path = "path/to/model.tflite"
with open(tflite_model_path, "wb") as f:
    f.write(tflite_model)

# Generate recommendations for a given recipe
data_nutrisi_input = np.array([[178.0, 2.1, 0.1, 40.6, 5.0, 0.5, 57.0]])
input_recipe_scaled = scaler.transform(data_nutrisi_input)

# Calculate similarity scores using the TFLite model
interpreter = tf.lite.Interpreter(model_content=tflite_model)
interpreter.allocate_tensors()

interpreter.set_tensor(interpreter.get_input_details()[0]['index'], input_recipe_scaled)
interpreter.invoke()
output_data = interpreter.get_tensor(interpreter.get_output_details()[0]['index'])

# Get recommended recipe indices
similarity_scores = cosine_similarity(input_recipe_scaled, recipe_data_scaled)
top_indices = np.argsort(similarity_scores, axis=1)[0][::-1][:10]

# Filter out irrelevant columns
relevant_columns = ['Energi', 'Protein', 'Lemak', 'Karbohidrat', 'Label', 'Gambar']
recommendations = data.iloc[top_indices][relevant_columns]

recommendations.head()