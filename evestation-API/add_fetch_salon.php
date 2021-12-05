<?php
include("dbConnect.php");
if(isset($_POST["salon_name"])){   //inserting salon
	$response["status"]=false;
	
	$city = $_POST["city"];
	$saloon_name = $_POST["salon_name"];
	$lat = $_POST["lat"];
	$lon = $_POST["lon"];
	
	$query = mysqli_query($conn,"INSERT INTO `salon` (`salon_name`,`latitude`,`longitude`,`city`)VALUES('$saloon_name','$lat','$lon','$city')");
	
	$check = mysqli_affected_rows($conn);
	if($check > 0){$response["status"]=true;}
	
}else{		//fetching all salons
	
	$response=array();
	
	$query = mysqli_query($conn,"SELECT * FROM `salon`");
	
	while($row = MYSQLI_FETCH_ASSOC($query)){
		
		array_push($response,array("salon_id"=>$row["salon_id"],"salon_name"=>$row["salon_name"],"lat"=>$row["latitude"],"lon"=>$row["longitude"],"city"=>$row["city"]));
		
	}
}

echo json_encode($response);
mysqli_close($conn);
exit();

?>