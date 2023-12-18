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
model_path = 'model._1.h5'  # Update with the correct path
model = tf.keras.models.load_model(model_path)

# Load dataset
data = pd.read_csv('/content/gizi_fix.xlsx - gizi_fix.csv')

# Preprocess dataset for nutritional recommendation
kolom_nutrisi = ['Energi', 'Protein', 'Lemak', 'Karbohidrat', 'Kalsium', 'Besi', 'Air']
data_nutrisi = data[kolom_nutrisi].fillna(0)
scaler = MinMaxScaler()
recipe_data_scaled = scaler.fit_transform(data_nutrisi)

# Define input dimensions for the model
input_dim = recipe_data_scaled.shape[1]

# Endpoint for nutritional recommendation
@app.route('/recnut', methods=['POST'])
def recnut():
    # Get input nutritional values from request
    nutritional_values = [request.json[col] for col in kolom_nutrisi]
    
    # Make prediction using the model
    prediction = model.predict(np.array([nutritional_values]))

    # Scale the input recipe using the same scaler
    input_recipe_scaled = scaler.transform(prediction)

    # Calculate similarity scores
    similarity_scores = cosine_similarity(input_recipe_scaled, recipe_data_scaled)

    # Get recommended recipe indices
    top_indices = np.argsort(similarity_scores, axis=1)[0][::-1][:10]

    # Select relevant columns for recommendations
    relevant_columns = ['Energi', 'Protein', 'Lemak', 'Karbohidrat', 'Kalsium', 'Besi', 'Air', 'Label', 'Gambar']
    recommendations = data.iloc[top_indices][relevant_columns]

    # Convert recommendations to JSON format
    rec_json = recommendations.to_dict('records')

    return {"message": "Nutritional recommendation success!", "status": 200, "data": rec_json}

# Default endpoint
@app.route('/')
def index():
    return jsonify({"message": "WELCOME TO RECIPE API"})

# Run the app
if __name__ == '__main__':
    app.run(host="0.0.0.0", debug=True, port=5000)