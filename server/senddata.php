<?php
require_once 'connection.php';
$GPioo = @$_GET['GPio'];
$statee= @$_GET['state'];
$query_update ="UPDATE esp SET state=$statee WHERE GPio=$GPioo";
$query_check = "SELECT * FROM esp WHERE GPio=$GPioo";
$resault=mysqli_query($connection,$query_check);
if (mysqli_num_rows($resault)>0){
    if (mysqli_query($connection,$query_update)){
    $key ="succset";
    }
    else {
     $key ="wrong";
    }
}
else {
     $query_register = "INSERT INTO esp (GPio, state) VALUES ('$GPioo','$statee')";
    if (mysqli_query($connection,$query_register)){
        $key ="succset";
    }
    else {
        $key ="wrong";
    }  
}


echo json_encode(array("response"=>$key));
mysqli_close($connection);


?>