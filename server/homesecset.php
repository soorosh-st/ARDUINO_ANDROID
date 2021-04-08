<?php
require_once 'connection.php';
$secure = @$_GET['secure'];
$query_update ="UPDATE home SET secure=$secure ";
$query_check = " SELECT * FROM home ";
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
     $query_register = "INSERT INTO home (secure) VALUES ('$secure')";
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