<?php
/**
 * Format class
 *
 * Help convert between various formats such as XML, JSON, CSV, etc.
 *
 * @author		leogs
 */
class Checker {

  function check_string($tocheck, &$assignedIfWorking, $field)
  {
    if(isset($tocheck[$field]) 
            and ($tocheck[$field] != null) 
            and is_string($tocheck[$field])){
      $assignedIfWorking[$field] = $tocheck[$field];
      return true;
    }else{
      return false;
    }
  }
	function check_float($tocheck, &$assignedIfWorking, $field)
  {
    if(isset($tocheck[$field]) 
            and ($tocheck[$field] != null) 
            and is_numeric($tocheck[$field])){
      $assignedIfWorking[$field] = floatval($tocheck[$field]);
      return true;
    }else{
      return false;
    }
  }
  function check_int($tocheck, &$assignedIfWorking, $field)
  {
    if(isset($tocheck[$field]) 
            and ($tocheck[$field] != null) 
            and is_numeric($tocheck[$field])){
      $assignedIfWorking[$field] = intval($tocheck[$field]);
      return true;
    }else{
      return false;
    }
  }
  function check_timestamp($tocheck, &$assignedIfWorking, $field)
  {
    if(isset($tocheck[$field]) 
            and ($tocheck[$field] != null) 
            and $this->is_mysqldate($tocheck[$field])){
      $assignedIfWorking[$field] = ($tocheck[$field]);
      return true;
    }else{
      return false;
    }
  }
  function is_mysqldate($string){
    // mysql date = Y-m-d h:m:s
//    $date = preg_split('#^([0-9]*)-([0-9]*)-([0-9]*) ([0-9]*):([0-9]*):([0-9]*)$#', $string);
//    $result = checkdate($date[1], $date[2], $date[0]);
    $date = date_parse($string);
//    print_r($date);
    return ($date['error_count'] == 0) and ($date['warning_count'] == 0);
  }
}

/* End of file format.php */