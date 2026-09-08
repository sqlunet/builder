#!/bin/python3

import os
from huggingface_hub import login
from huggingface_hub import create_bucket
from dotenv import load_dotenv

# Load environment variables from the .env file
# Get the token from the environment
load_dotenv()
token=os.getenv("HF_TOKEN")

login(token=token , add_to_git_credential=False)

bucket='semantikos/semantikos'
b=create_bucket(bucket)
print(b)
