#  RabbitMQ Setup Guide on AWS EC2 (Ubuntu 22.04+)

This guide helps you install RabbitMQ with the management console on an AWS EC2 Ubuntu instance.

---

## 📦 Prerequisites

- ✅ AWS EC2 instance (Ubuntu 22.04 or later)
- ✅ Open inbound ports in EC2 Security Group:
    - `22` for SSH
    - `5672` for AMQP protocol
    - `15672` for Management UI

---

## 📦 Step-by-Step Setup

### 1. Connect to EC2 Instance

```bash
ssh -i path/to/your/pemkey/your-key.pem ubuntu@your-ec2-public-ip
```
or connect via AWS Management Console

### 2. Update System

```bash
sudo apt update && sudo apt upgrade -y
```

### 3. Install Erlang (RabbitMQ dependency)

```bash
sudo apt install erlang -y
```

### 4.  Add RabbitMQ Signing Key and Repository

```bash
# Import signing key
wget -O- https://packagecloud.io/rabbitmq/rabbitmq-server/gpgkey | sudo apt-key add -

# Add repository
sudo tee /etc/apt/sources.list.d/rabbitmq.list <<EOF
deb https://packagecloud.io/rabbitmq/rabbitmq-server/ubuntu/ $(lsb_release -cs) main
EOF

# Update package list
sudo apt update
```

### 5.   Install RabbitMQ Server

```bash
sudo apt install rabbitmq-server -y
```

### 6.    Enable & Start RabbitMQ

```bash
sudo systemctl enable rabbitmq-server
sudo systemctl start rabbitmq-server
sudo systemctl status rabbitmq-server
```

### 7.    Enable RabbitMQ Management Plugin

```bash
sudo rabbitmq-plugins enable rabbitmq_management
```

### 8.    Create Admin User

```bash
# Replace 'username' and 'password' with desired credentials
sudo rabbitmqctl add_user username password
sudo rabbitmqctl set_user_tags username administrator
sudo rabbitmqctl set_permissions -p / username ".*" ".*" ".*"
```

### 9.    Create Virtual Host

```bash
# Replace 'username' with actual user
# for dev environment replace 'live-streaming-platform' with 'live-streaming-platform-dev'
# for stg environment replace 'live-streaming-platform' with 'live-streaming-platform-stg'
# for prod environment leave 'live-streaming-platform' as is
sudo rabbitmqctl add_vhost live-streaming-platform
sudo rabbitmqctl set_permissions -p live-streaming-platform username ".*" ".*" ".*"
```





#  FFMPEG Setup Guide on Video Encoding

This guide helps you install ffmpeg build which will be used to encode the uploaded video in various resolutions
---

## 📦 Step-by-Step Setup

### 1. Download the ffmepg build zip file from below link 

```
https://www.gyan.dev/ffmpeg/builds/ffmpeg-git-full.7z
```

### 2. Add the 'ffmpeg.exe' parent bin folder path to environment variable

```
PATH = "C:\ffmpeg-2025-04-21-git-9e1162bdf1-full_build\bin"
```

### 3. Run below command on CMD to validate the installation

```
ffmpeg --version
```

