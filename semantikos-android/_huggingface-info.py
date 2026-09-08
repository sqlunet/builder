#!/bin/python3

from huggingface_hub import bucket_info
from huggingface_hub import list_bucket_tree

bucket='semantikos/semantikos'

info=bucket_info(bucket)
print(info)

for f in list_bucket_tree(bucket):
    print(f.path, f.size, f.mtime)

