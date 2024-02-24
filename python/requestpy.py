import requests
import threading
import time
import yaml

def read_config(file_path):
    with open(file_path, 'r') as file:
        config = yaml.safe_load(file)
    return config

config = read_config('config.yaml')
num_threads = config['num_threads'] # Number of threads
urls = config['urls'] # List of URLs to make requests to
num_calls = config['num_calls_requestpy'] # Number of API calls per thread

def make_request(url): # Function to make API requests
    start_time = time.time()
    response = requests.get(url)
    duration = time.time() - start_time
    # print(f"Thread: {threading.current_thread().name}, URL: {url}, Duration: {duration:.3f} seconds")

start_collection_time = time.time() # Start time of data collection
print(f"Data collection started at: {time.strftime('%Y-%m-%d %H:%M:%S')}")

threads = [] # Create and start threads
for i in range(num_threads):
    thread = threading.Thread(target=lambda: [make_request(url) for _ in range(num_calls) for url in urls])
    threads.append(thread)
    thread.start()
    # print(f"Started thread: {thread.name}")

for thread in threads: # Wait for all threads to complete
    thread.join()

end_collection_time = time.time() # End time of data collection
total_duration = end_collection_time - start_collection_time
print(f"Data collection ended at: {time.strftime('%Y-%m-%d %H:%M:%S')}")
print(f"Total duration: {total_duration:.3f} seconds")
print("All requests completed.")
