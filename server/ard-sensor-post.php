<?php
require_once 'connection.php';
$state = @$_POST['state'];

$query_update ="UPDATE ard SET state=$state ";

if (mysqli_query($connection,$query_update))
    echo "good";
else 
    echo "bad";

mysqli_close ($connection);

?>