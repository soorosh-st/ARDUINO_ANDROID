<?php
require_once 'connection.php';
if ($_SERVER["REQUEST_METHOD"] == "GET") {
$resault;
$query = "SELECT * FROM esp  ";

if (mysqli_query($connection,$query)){
    
   $resault = $connection->query($query);
}
else {
    $resault=false;
}
if ($resault) {
   
    while ($row = $resault->fetch_assoc()) {
        
       $rows[$row["GPio"]] = $row["state"];
    }
}
    echo json_encode($rows);
    
    
    
}


mysqli_close($connection);

?>