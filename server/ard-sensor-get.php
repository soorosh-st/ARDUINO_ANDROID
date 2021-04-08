<?php 
require_once 'connection.php';
$query="SELECT * FROM home";
$result=mysqli_query($connection,$query);
if ($result){
    $res=$result->fetch_assoc();
}
else {
    echo "failed";
}

echo json_encode($res);
mysqli_close($connection);
?>