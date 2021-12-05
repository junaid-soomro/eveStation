<?php
$id = $_POST["ID"];
$name = $_POST["Name"];
$email = $_POST["Email"];
$address = $_POST["address"];
$phone = $_POST["phone"];
$username = $_POST["username"];

include("dbConnect.php");
$response["success"]=false;
$response["PASSWORD"]=false;

if(isset($_POST["password"])){

$password = $_POST["password"];
$new_password = $_POST["new_password"];

$getID=mysqli_query($conn,"SELECT `password` FROM `users` WHERE `user_id` = $id");

while($row = mysqli_fetch_array($getID,MYSQLI_ASSOC)){
    
    $real_pass=$row['password'];
        
}


if($real_pass==$password){
    
    $result2 = mysqli_query($conn,"UPDATE `users` SET `name` = '$name' ,`email` = '$email',`password`='$new_password',`username`='$username',`phone`='$phone',`address`='$address' WHERE `user_id`='$id'");
    
	$check1 = mysqli_affected_rows($conn);
	if($check1 > 0){
	$response["success"]=true;
	}
	}
else{
    
    $response["PASSWORD"]=true;
    
	}
}
else{
			
			    $result3 = mysqli_query($conn,"UPDATE `users` SET `name` = '$name' ,`email` = '$email',`username`='$username',`phone`='$phone',`address`='$address' WHERE `user_id`='$id'");
    $check2 = mysqli_affected_rows($conn);
	
	if($check2 > 0){
	$response["success"]=true;
	}
	$response["success"]=true;

	
}

echo json_encode($response);
mysqli_close($conn);
exit();
?>