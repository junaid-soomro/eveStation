<?php
include("dbConnect.php");
$response["status"]=false;

if(isset($_POST["operation"])){
    
    $service_id = $_POST["service_id"];
    
    mysqli_query($conn,"DELETE FROM `service_detail` WHERE `serv_id`='$service_id'");
    
    $check = mysqli_affected_rows($conn);
    if($check > 0){
        $response["status"]= true;
        
    }
}
else if(isset($_POST["service_image"])) {
    
    $service_id = $_POST["service_id"];
    $service_name = $_POST["service_name"];
    $service_price = $_POST["service_price"];
    $service_detail = $_POST["service_detail"];
    $image = $_POST["service_image"];
    
    mysqli_query($conn,"UPDATE `service_detail` SET `serv_name`='$service_name'
    ,`serv_price`='$service_price',`serv_detail`='$service_detail',`image`='$image' WHERE `serv_id`='$service_id'");
    
    $check = mysqli_affected_rows($conn);
    if($check > 0){
        $response["status"]= true;
        
    }
}

else {
    
    $service_id = $_POST["service_id"];
    $service_name = $_POST["service_name"];
    $service_price = $_POST["service_price"];
    $service_detail = $_POST["service_detail"];
    
    mysqli_query($conn,"UPDATE `service_detail` SET `serv_name`='$service_name'
    ,`serv_price`='$service_price',`serv_detail`='$service_detail' WHERE `serv_id`='$service_id'");
    $check = mysqli_affected_rows($conn);
    if($check > 0){
        $response["status"]= true;
        
    }
    
    
}

echo json_encode($response);
mysqli_close($conn);
exit();

?>