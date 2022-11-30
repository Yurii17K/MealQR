import compress_fasttext
from flask import Flask, request, jsonify, abort
import re
import numpy as np
from numpy.linalg import norm

app = Flask(__name__)

model = compress_fasttext.models.CompressedFastTextKeyedVectors.load('small_model_v2')

stopwords = []

def cosine_similarity(A, B):
    all_zeros = not (np.any(A) and np.any(B))
    if all_zeros:
        return 0.0
    return (np.dot(A, B) / (norm(A) * norm(B)))


with open("stopwords.txt", 'r', encoding="utf-8") as stopwords_file:
    stopwords = [line.rstrip() for line in stopwords_file]


def process_line(text_line):
    return re.sub('|'.join(r'\b%s\b' % re.escape(s) for s in stopwords),
                  "", text_line)


def get_dish_similarity(dish1, dish2):
    dish1_stripped = process_line(dish1).split()
    dish2_stripped = process_line(dish2).split()
    return cosine_similarity(model.get_sentence_vector(dish1_stripped),model.get_sentence_vector(dish2_stripped))

def get_human_dish_similarity(dish1,dish2):
    similarity = get_dish_similarity(dish1,dish2)
    print("Similarity of: ",dish1, " AND ",dish2,"==== ",similarity)

def vectorize(dish_name, dish_description):
    dish_string = dish_name+" ||| "+dish_description
    dish_stripped = process_line(dish_string).split()
    return model.get_sentence_vector(dish_stripped).tolist()

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/get-vector', methods=['POST'])
def get_vector():
    try:
        request_json = request.get_json()
        dish_name = request_json["dish_name"]
        dish_description = request_json["dish_description"]
    except:
        abort(400)
    return vectorize(dish_name,dish_description)

@app.route('/get-dish-similarity', methods=['POST'])
def get_dish_similarity():
    try:
        request_json = request.get_json()
        #print(request_json)
        dish1_name = request_json["dish1_name"]
        dish1_description = request_json["dish1_description"]
        dish2_name = request_json["dish2_name"]
        dish2_description = request_json["dish2_description"]
        #print(dish1_name)
        #print(dish2_name)
        #print(cosine_similarity(vectorize(dish1_name,dish1_description),vectorize(dish2_name,dish2_description)))
    except:
        abort(400)
    return jsonify(cosine_similarity(vectorize(dish1_name,dish1_description),vectorize(dish2_name,dish2_description)))