<?php
include("dbConnect.php");

$response["status"]=false;

$cust_id = $_POST["customer_id"];
$status = $_POST["status"];
$service_id = $_POST["service_id"];
$jsonarray = json_decode($service_id,true);

foreach($jsonarray as $id){
	
	$query = mysqli_query($conn,"UPDATE `appointments` SET `appoint_status`='$status' WHERE `customer_id`='$cust_id' AND `service_id`=$id");
	
}
$check = mysqli_affected_rows($conn);

if($check>0){
	
	$response["status"]=true;
	
}



echo json_encode($response);
mysqli_close($conn);
exit();

?>