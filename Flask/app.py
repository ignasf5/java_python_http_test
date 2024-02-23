from flask import Flask, jsonify, request

app = Flask(__name__)

# Sample data - list of dictionaries
books = [
    {"id": 1, "title": "Python Programming", "author": "John Doe"},
    {"id": 2, "title": "Java Programming", "author": "Jane Smith"}
]

# API endpoint to get all books
@app.route('/books', methods=['GET'])
def get_books():
    return jsonify(books)

# API endpoint to get a specific book by ID
@app.route('/books/<int:id>', methods=['GET'])
def get_book(id):
    book = next((book for book in books if book['id'] == id), None)
    if book:
        return jsonify(book)
    return jsonify({"message": "Book not found"}), 404

# API endpoint to create a new book
@app.route('/books', methods=['POST'])
def create_book():
    data = request.get_json()
    books.append(data)
    return jsonify(data), 201

if __name__ == '__main__':
    app.run(debug=True)
