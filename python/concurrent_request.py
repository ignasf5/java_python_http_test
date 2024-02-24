import requests
import concurrent.futures
import threading
import time
import yaml

def read_config(file_path):
    with open(file_path, 'r') as file:
        config = yaml.safe_load(file)
    return config

config = read_config('config.yaml')
num_threads = config['num_threads'] # Number of concurrent threads
urls = config['urls'] # List of URLs to make requests to
num_calls_concunernt = config['num_calls_requestpy'] # Number of API calls per thread

# Function to generate a list of URLs
def generate_urls(urls,num_calls):
    urls *= num_calls  # Repeat the list 50 times to get 100 identical items
    return urls

# Function to make API requests
def make_request(url):
    start_time = time.time()
    thread_num = threading.get_ident()  # Get the thread number
    response = requests.get(url)
    duration = time.time() - start_time
    # print(f"Thread: {thread_num}, URL: {url}, Duration: {duration:.3f} seconds")

# Start time of data collection
start_collection_time = time.time()
print(f"Data collection started at: {time.strftime('%Y-%m-%d %H:%M:%S')}")

urls = generate_urls(urls,num_calls_concunernt) # Generate a list of URLs with 100 identical items

with concurrent.futures.ThreadPoolExecutor(max_workers=num_threads) as executor: # Create a ThreadPoolExecutor
    # Submit tasks for each URL
    futures = [executor.submit(make_request, url) for url in urls]

    # Wait for all tasks to complete
    for future in concurrent.futures.as_completed(futures):
        # Handle any exceptions that occurred during execution
        try:
            future.result()
        except Exception as e:
            print(f"An error occurred: {e}")

# End time of data collection
end_collection_time = time.time()
total_duration = end_collection_time - start_collection_time
print(f"Data collection ended at: {time.strftime('%Y-%m-%d %H:%M:%S')}")
print(f"Total duration: {total_duration:.3f} seconds")
print("All requests completed.")