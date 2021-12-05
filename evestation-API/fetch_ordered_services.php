<?php
include("dbConnect.php");
if(isset($_POST["customer_id"])){
$response = array();
$user_id = $_POST["customer_id"];
$query = mysqli_query($conn,"SELECT `service_id`,service_detail.serv_name FROM `appointments` INNER JOIN `service_detail` ON(`customer_id`='$user_id') AND (service_detail.serv_id=service_id)  ");
}
while($row = MYSQLI_FETCH_ASSOC($query)){
    
    array_push($response,array("service_id"=>$row["service_id"],"service_name"=>$row["serv_name"]));
    
}

echo json_encode($response);
mysqli_close($conn);
exit();



?>