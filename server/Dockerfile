FROM python:3
WORKDIR /src/app

COPY helpers.py .
COPY server.py .
COPY threads.py .

EXPOSE 8080
CMD ["python", "-u", "./server.py"]