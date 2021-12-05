<?php
include("dbConnect.php");
if(isset($_POST["service_name"])){   //inserting services
	$response["status"]=false;
	
	$service_name = $_POST["service_name"];
	$serv_detail = $_POST["detail"];
	$price = $_POST["price"];
	$image = $_POST["image"];
	
	$query = mysqli_query($conn,"INSERT INTO `service_detail` (`serv_name`,`serv_detail`
	,`serv_price`,`image`)VALUES('$service_name','$serv_detail','$price','$image')");
	
	$check = mysqli_affected_rows($conn);
	if($check > 0){$response["status"]=true;}
	
}else{		//fetching all services
	
	$response=array();
	
	$query = mysqli_query($conn,"SELECT * FROM `service_detail`");
	
	while($row = MYSQLI_FETCH_ASSOC($query)){
		
		array_push($response,array("service_id"=>$row["serv_id"],"detail"=>$row["serv_detail"],
		"name"=>$row["serv_name"],"price"=>$row["serv_price"],"image"=>$row["image"]));
		
	}
}

echo json_encode($response);
mysqli_close($conn);
exit();

?>