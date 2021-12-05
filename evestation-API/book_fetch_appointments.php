<?php
include("dbConnect.php");
if(isset($_POST["location"])){
	$response["status"]=false;
	$cust_id = $_POST["customer_id"];
	$longitude = $_POST["longitude"];
	$latitude = $_POST["latitude"];
	$price = $_POST["price"];
	$time = $_POST["time"];
	$date = $_POST["date"];
	
	$query = mysqli_query($conn,"INSERT INTO `appointments` (`latitude,`longitude`,`total_price`
	,`date`,`time`,`customer_id`)VALUES('$latitude','$longitude','$price','$date','$time','$cust_id')");
	
	$check = mysqli_affected_rows($conn);
	if($check > 0){$response["status"]=true;}
	
}else{		//fetching all appoints
	
	$response=array();
	
	$query = mysqli_query($conn,"SELECT `appoint_id`,users.name,users.user_id, `appoint_status`, `latitude`,`longitude`, `payment_status`, `total_price`, `time`, `date` FROM `appointments` INNER JOIN `users` ON(appointments.customer_id=users.user_id) WHERE `appoint_status`='pending' GROUP BY customer_id ");
	
	while($row = MYSQLI_FETCH_ASSOC($query)){
		
		array_push($response,array("appoint_id"=>$row["appoint_id"],"latitude"=>$row["latitude"],"longitude"=>$row["longitude"],
		"price"=>$row["total_price"],"time"=>$row["time"],"date"=>$row["date"],"appoint_status"=>
		$row["appoint_status"],"customer_name"=>$row["name"],"payment_status"=>$row["payment_status"],"user_id"=>$row["user_id"]));
		
	}
}
if(isset($_POST["cust_id"])){ //fetching appoints for a particular customer
	
	$cust_id = $_POST["cust_id"];
	
	$response=array();
	
	$query = mysqli_query($conn,"SELECT `appoint_id`,customer.name, `appoint_status`, `location`, `payment_status`, `total_price`, `time`, `date` FROM `appointments` INNER JOIN `customer` ON(appointments.customer_id=$cust_id)");
	
	while($row = MYSQLI_FETCH_ASSOC($query)){
		
		array_push($response,array("appoint_id"=>$row["appoint_id"],"location"=>$row["location"],
		"price"=>$row["total_price"],"time"=>$row["time"],"date"=>$row["date"],"appoint_status"=>
		$row["appoint_status"],"customer_name"=>$row["name"]));
		
	}
	
}

echo json_encode($response);
mysqli_close($conn);
exit();

?>