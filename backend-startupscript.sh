sudo apt-get update &&
sudo apt-get -y install openjdk-8-jdk &&
sudo mkdir application &&
sudo chmod a+rwx application &&
gsutil cp gs://builds-repository/backend/hotelapp-latest.jar application/ &&
sudo chmod +r+w+x application/hotelapp-latest.jar &&
java -jar application/hotelapp-latest.jar