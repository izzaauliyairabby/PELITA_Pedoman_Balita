# Import necessary libraries
from flask import Flask, request, jsonify
import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics.pairwise import cosine_similarity

# Create Flask app
app = Flask(__name__)

# Load machine learning model
model_path = "model.h5"  # Update with the correct path
model = tf.keras.models.load_model(model_path)

# Load dataset
data = pd.read_csv("dataset_gizi.csv", encoding="latin-1", delimiter=",")
data.rename(columns={"Nama Pangan": "Label"}, inplace=True)

# Preprocess dataset for nutritional recommendation
kolom_nutrisi = ["Energi", "Protein", "Lemak", "Karbohidrat"]
data_nutrisi = data[kolom_nutrisi].copy()
data_nutrisi = data_nutrisi.fillna(0)  # Replace missing values with 0 or appropriate values
scaler = MinMaxScaler()
recipe_data_scaled = scaler.fit_transform(data_nutrisi)

# Define input dimensions for the model
input_dim = recipe_data_scaled.shape[1]

# Endpoint for nutritional recommendation
@app.route("/predict", methods=["POST"])
def predict():
    try:
        # Get input nutritional values from request
        nutritional_values = [request.json[col] for col in kolom_nutrisi]

        # Scale the input values using the same scaler
        scaled_nutritional_values = scaler.transform([nutritional_values])

        # Make prediction using the model
        prediction = model.predict(np.array(scaled_nutritional_values))

        # Reshape the prediction array to 2D
        prediction_2d = prediction.reshape(1, -1)

        # Calculate similarity scores
        similarity_scores = cosine_similarity(prediction_2d, recipe_data_scaled)

        # Get recommended recipe indices
        top_indices = np.argsort(similarity_scores, axis=1)[0][::-1][:10]

        # Select relevant columns for recommendations
        # Adjust this list based on your dataset columns
        relevant_columns = ["Energi", "Protein", "Lemak", "Karbohidrat", "Label", "Gambar"]

        recommendations = data.iloc[top_indices][relevant_columns]

        # Convert recommendations to JSON format
        rec_json = recommendations.to_dict("records")

        return jsonify({
            "error": False,
            "message": "Nutritional recommendation success!",
            "data": rec_json,
        }), 200

    except Exception as err:
        error_message = str(err)
        print("[ERROR]", error_message)
        return jsonify({
            "error": True,
            "message": error_message,
        }), 500

# Default endpoint
@app.route("/")
def index():
    return jsonify({
            "error": False,
            "message": "Success fetching the API",
    }), 200

# Run the app
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
