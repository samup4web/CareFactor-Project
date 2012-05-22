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

    private $data_table = 'cf_food_bank';
    private $user_profile_table = 'user_profiles';

    function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    function store_food_resource($new_foodbank_item)
    {
        $query = $this->db->insert_string($this->data_table, $new_foodbank_item);
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
                'error' => array(
                    'code' => $error_code,
                    'message' => $error_message),
                'code' => 403);
            return $result;
        }
    }
    
    function update_notification($notification)
    {
        $query = $this->db->insert_string('system_notifications', $notification);
        $res = $this->db->query($query);
        if ($res)
        {
            $result['message'] = 'the request complete and ' . $this->db->affected_rows() . ' new value(s) inserted';
            $result['code'] = 200;
            //return $result;
        } else
        {
            $error_code = $this->db->_error_number();
            $error_message = $this->db->_error_message();
            $result = array(
                'error' => array(
                    'code' => $error_code,
                    'message' => $error_message),
                'code' => 403);
           // return $result;
        }
    }
    
    function remove_wish_list($id){
        
        //$this->db->delete('wish_list', array('wishlist_id' => $id)); 
        //return "deleted!";
        
    }
     function store_wish_list($new_wish_list_item)
    {
        $query = $this->db->insert_string('wish_list', $new_wish_list_item);
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
                'error' => array(
                    'code' => $error_code,
                    'message' => $error_message),
                'code' => 403);
            return $result;
        }
    }

    function update_user_profile($userdata)
    {
        $response = "";
        $user_id = $userdata['user_id'];
        unset($userdata['user_id']);

        $update = $this->db->where('user_id', $user_id)
                ->update($this->user_profile_table, $userdata);

        $response = "profile updated!";


        return $response;
    }

//   
}