import unittest
import json
from app import app, books

class TestAPI(unittest.TestCase):

    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True

    def test_get_books(self):
        response = self.app.get('/books')
        data = json.loads(response.data)
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(data), len(books))

    def test_get_book(self):
        response = self.app.get('/books/1')
        data = json.loads(response.data)
        self.assertEqual(response.status_code, 200)
        self.assertEqual(data['id'], 1)

    def test_get_nonexistent_book(self):
        response = self.app.get('/books/100')
        self.assertEqual(response.status_code, 404)
        data = json.loads(response.data)
        self.assertEqual(data['message'], 'Book not found')

    def test_create_book(self):
        new_book = {"id": 3, "title": "JavaScript Programming", "author": "Jake Johnson"}
        response = self.app.post('/books', json=new_book)
        self.assertEqual(response.status_code, 201)
        self.assertIn(new_book, books)

if __name__ == '__main__':
    unittest.main()
