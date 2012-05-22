<?php

defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Storer
 *
 * This is an example of a few basic user interaction methods you could use
 * all done with a hardcoded array.
 *
 * @package		CodeIgniter
 * @category	Model
 * @author		leogs
 */
class Storer extends CI_Model {

    private $sensor_measurement = 'gateway_feeds';

    function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    function store_measurement($measurement)
    {
        //it is assumed that $measurement is correctly formated:
        /*
         * sensor_id, gateway_id = str,
         * pos_longitude, pos_latitude = float
         * data_value = int
         * timestamp = timestamp
         */
      
        $query = $this->db->insert_string($this->sensor_measurement, $measurement);
        $res = $this->db->query($query);
      
        if ($res)
        {
            $result['message'] = 'the request complete and ' . $this->db->affected_rows() . ' new value(s) inserted';
            $result['code'] = 200;
            return $result;
        } else
        {
            $error_code = $this->db->_error_number();
            $error_message = $this->db->_error_message();
            $result = array(
                'error' => array('code' => $error_code,
                    'message' => $error_message),
                'code' => 403);
            return $result;
        }
    }

//    function store_batch_measurement($measurement_batch)
//    {
//        //$measurement_batch is array of records/rows
//        print_r($measurement_batch);
//        $measurement_batch = $this->escape($measurement_batch);
//        $query = $this->db->insert_batch($this->sensor_measurement, $measurement_batch);
//        $res = $this->db->query($query);
//        if ($res)
//        {
//            $result['message'] = 'the request complete and ' . $this->db->affected_rows() . ' new value(s) inserted';
//            $result['code'] = 200;
//            return $result;
//        } else
//        {
//            $error_code = $this->db->_error_number();
//            $error_message = $this->db->_error_message();
//            $result = array(
//                'error' => array('code' => $error_code,
//                    'message' => $error_message),
//                'code' => 403);
//            return $result;
//        }
//    }

//  function store_measurement_old_way($measurement)
//  {
//    try
//    {
//      //$this->db->insert($this->sensor_measurement, $measurement);
//      $insert = $this->db->insert_string($this->sensor_measurement, $measurement);
//      $this->db->query($insert);
//      $result['message'] = $insert.'<br /> the request complete and '.$this->db->affected_rows().'new value(s) inserted';
//      $result['code'] = 200;
//      return $result;
//    } catch (Exception $e)
//    {
//      $result['message'] = $e;
//      $result['code'] = 403;
//      return $result;
//    }
//  }
}