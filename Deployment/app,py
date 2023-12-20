import pandas as pd
import numpy as np
import tensorflow as tf
from tensorflow import keras
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics.pairwise import cosine_similarity
from tensorflow.keras.optimizers import Adam
import matplotlib.pyplot as plt
from flask import Flask, jsonify, request

# Load your data
data = pd.read_csv('/content/gizi_fix.xlsx - gizi_fix.csv')
data.rename(columns={'Nama Pangan': 'Label'}, inplace=True)
kolom_nutrisi = ['Energi', 'Protein', 'Lemak', 'Karbohidrat', 'Kalsium', 'Besi', 'Air']
data_nutrisi = data[kolom_nutrisi].fillna(0)
scaler = MinMaxScaler()
recipe_data_scaled = scaler.fit_transform(data_nutrisi)

# Split the dataset into training and validation sets
train_data, val_data = train_test_split(recipe_data_scaled, test_size=0.3, random_state=42)

# Build the recommendation model
input_dim = recipe_data_scaled.shape[1]
model = tf.keras.models.Sequential([
    tf.keras.layers.Conv1D(filters=32, kernel_size=5, strides=1, padding='causal', activation='relu',
                           input_shape=[None, 1]),
    tf.keras.layers.LSTM(64, return_sequences=True),
    tf.keras.layers.LSTM(64, return_sequences=True),
    tf.keras.layers.Dense(30, activation='relu'),
    tf.keras.layers.Dense(10, activation='relu'),
    tf.keras.layers.Dense(1),
])

model.compile(loss='mean_squared_error', optimizer=Adam())

# Train the model
history = model.fit(train_data, train_data, epochs=20, batch_size=32, validation_data=(val_data, val_data))

# Save the model in TensorFlow Lite format
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()
with open("model.tflite", "wb") as f:
    f.write(tflite_model)

# Flask web application
app = Flask(__name__)

# Load the TensorFlow Lite model
interpreter = tf.lite.Interpreter(model_content=tflite_model)
interpreter.allocate_tensors()

# Define the API endpoint for prediction
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Get input data from the request
        input_data = request.json['input_data']

        # Preprocess the input data
        input_data = np.array(input_data).reshape(1, -1)
        input_data_scaled = scaler.transform(input_data)

        # Make predictions using the TensorFlow Lite model
        interpreter.set_tensor(interpreter.get_input_details()[0]['index'], input_data_scaled.astype(np.float32))
        interpreter.invoke()
        output_data = interpreter.get_tensor(interpreter.get_output_details()[0]['index'])

        # Return the predictions as JSON
        return jsonify({'prediction': output_data.tolist()})

    except Exception as e:
        return jsonify({'error': str(e)})

# Run the Flask app
if __name__ == '__main__':
    app.run(debug=True)
