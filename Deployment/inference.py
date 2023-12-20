import requests
import json

# URL of the Flask app
url = 'http://127.0.0.1:5000/predict'

# Sample input data for inference
input_data = {
    'input_data': [178.0, 2.1, 0.1, 40.6, 5.0, 0.5, 57.0]
}

# Convert input data to JSON format
json_data = json.dumps(input_data)

# Set the headers
headers = {'Content-Type': 'application/json'}

# Make the POST request
response = requests.post(url, data=json_data, headers=headers)

# Check the response
if response.status_code == 200:
    result = response.json()
    print("Prediction:", result['prediction'])
else:
    print("Error:", response.json())
