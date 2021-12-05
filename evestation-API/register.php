<?php
$password = $_POST["password"];
$email = $_POST["email"];
$name = $_POST["name"];
$type = $_POST["user_type"];
$phone = $_POST["phone"];
$address = $_POST["address"];
$username = $_POST["username"];


include("dbConnect.php");
$response=array();
$response["success"]=false;
$check=mysqli_query($conn,"SELECT * FROM `users` WHERE `Email`='$email'");
$affected=mysqli_affected_rows($conn);
if($affected>0){
  $response["status"]="USERNAME";
}
else{
  
  $result=mysqli_query($conn,"INSERT INTO `users` (`name`, `password`, `email`, `type`,`phone`,`address`,`username`) VALUES ('$name', '$password', '$email', '$type','$phone','$address','$username')");
  
  $response["success"]=true;
}
echo json_encode($response);#encoding RESPONSE into a JSON and returning.
mysqli_close($conn);
exit();
?>