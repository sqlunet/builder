#!/bin/python3

import glob
import os
from pathlib import Path
from huggingface_hub import login
from huggingface_hub import batch_bucket_files
from dotenv import load_dotenv

# Load environment variables from the .env file
# Get the token from the environment
load_dotenv()
token=os.getenv("HF_TOKEN")

login(token=token, add_to_git_credential=False)

def upload(files):
  bucket='semantikos/semantikos'
  b=batch_bucket_files(
    bucket,
    add=files,
  )
  print(b)
  
#dirs = ("db", "db-oewn", "db-wn", "db-vn", "db-sn", "db-fn")
#files=[(f, Path(f).name) for d in dirs for f in glob.glob(f"{d}/*", recursive=True) if os.path.isfile(f) and not f.endswith((".db", ".sh"))]

dir="db-all"
files=[(f, Path(f).name) for f in glob.glob(f"{dir}/*", recursive=True) if os.path.isfile(f) and not f.endswith((".db", ".sh"))]

for p in files:
    print(p)
upload(files)

