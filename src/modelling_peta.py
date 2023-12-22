import pandas as pd
import numpy as np
import tensorflow as tf
from tensorflow import keras
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt
from sklearn.metrics.pairwise import cosine_similarity
from tensorflow.keras.optimizers import Adam

# Load Dataset
data = pd.read_csv("dataset_gizi.csv", encoding="latin-1", delimiter=",")

# Rename the column
data.rename(columns={"Nama Pangan": "Label"}, inplace=True)

# For input
kolom_nutrisi = ["Energi", "Protein", "Lemak", "Karbohidrat"]
data_nutrisi = data[kolom_nutrisi].copy()

data_nutrisi = data_nutrisi.fillna(0)  # Replace missing values with 0 or appropriate values

# Continue with the scaling and other steps
scaler = MinMaxScaler()
recipe_data_scaled = scaler.fit_transform(data_nutrisi)

# Split the dataset into training and validation sets
train_data, val_data = train_test_split(recipe_data_scaled, test_size=0.3, random_state=42)

# Build the recommendation model
input_dim = recipe_data_scaled.shape[1]

model = tf.keras.models.Sequential([
    tf.keras.layers.Conv1D(filters=32, kernel_size=5, strides=1, padding="causal", activation="relu",
                            input_shape=[None, 1]),
    tf.keras.layers.LSTM(64, return_sequences=True),
    tf.keras.layers.LSTM(64, return_sequences=True),
    tf.keras.layers.Dense(30, activation="relu"),
    tf.keras.layers.Dense(10, activation="relu"),
    tf.keras.layers.Dense(1),
])

model.compile(loss="mean_squared_error", optimizer=Adam())

# Train the model
history = model.fit(train_data, train_data, epochs=20, batch_size=32, validation_data=(val_data, val_data))

def plot_loss(history):
    plt.plot(history.history["loss"], label="Training Loss")
    plt.plot(history.history["val_loss"], label="Validation Loss")
    plt.xlabel("Epoch")
    plt.ylabel("Loss")
    plt.title("Training and Validation Loss")
    plt.legend()
    plt.grid(True)
    plt.show()

plot_loss(history)

# Generate recommendations for a given recipe
data_nutrisi = np.array([[178.0, 2.1, 0.1, 40.6]])  # Replace with your input recipe

# Scale the input recipe using the same scaler
input_recipe_scaled = scaler.transform(data_nutrisi)

# Calculate similarity scores
similarity_scores = cosine_similarity(input_recipe_scaled, recipe_data_scaled)

# Get recommended recipe indices
top_indices = np.argsort(similarity_scores, axis=1)[0][::-1][:10]  # Get top 10 indices

# Filter out irrelevant columns
relevant_columns = ["Energi", "Protein", "Lemak", "Karbohidrat", "Label", "Gambar"]
recommendations = data.iloc[top_indices][relevant_columns]

print(recommendations)

# Save the model
model.save("model.h5")
