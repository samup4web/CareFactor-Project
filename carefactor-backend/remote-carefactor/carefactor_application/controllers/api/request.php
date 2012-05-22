<?php

defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * request
 *
 * This is an example of a few basic user interaction methods you could use
 * all done with a hardcoded array.
 *
 * @package		CodeIgniter
 * @subpackage	Rest Server
 * @category	Controller

 */
// This can be removed if you use __autoload() in config.php OR use Modular Extensions


require APPPATH . '/libraries/REST_Controller.php';

class Request extends REST_Controller {

    /**
     * get search data from food bank producers...
     * 
     * @param 
     * producer
     * location
     * category
     * fields - Json array of fields needed
     * orderby
     * order - Asc/Desc
     * 
     */
    function __construct()
    {
        parent::__construct();
        $this->ci = & get_instance();
        $this->load->library('tank_auth');
        $this->lang->load('tank_auth');
    }

    function foodbank_get()
    {

        $recent = false;
        $recent = $this->get('recent');

        if ($recent == "true")
        {
            $this->load->model('Retriever');
            $foodbank_items = $this->Retriever->get_foodbank_recent();
            $this->response($foodbank_items, 200);
            return;
        }

        $query_producer = false;
        $query_producer = $this->get('query_producer');

        $query_category = false;
        $query_category = $this->get('query_category');

        $query_region = false;
        $query_region = $this->get('query_region');

        //sort filters...

        $sort_expiry_date = false;
        $sort_expiry_date = $this->get('sort_expiry_date');

        $sort_producer = false;
        $sort_producer = $this->get('sort_producer');

        $sort_sumitted_date = false;
        $sort_sumitted_date = $this->get('sort_sumitted_date');

        $sort_proximity = false;
        $sort_proximity = $this->get('sort_proximity');

        $fields = $this->_create_field_filter();
        if ($fields === false)
        {//if no filter is specified
            //then create a default filter...
            $fields = array('id', 'producer_id', 'category', 'foodname', 'description', 'expiry_date', 'is_free', 'price',
                'quantity', 'unit', 'pick_up_date_start', 'pick_up_date_end', 'currency', 'update_timestamp');
        }



        try
        {
            $this->load->model('Retriever');
            $foodbank_items = $this->Retriever->get_foodbank_data($fields, $query_producer, $query_category, $query_region, $sort_expiry_date, $sort_producer, $sort_sumitted_date, $sort_proximity, $sort_proximity);
            $this->response($foodbank_items, 200);
        } catch (Exception $e)
        {
            $this->response($e, 403);
        }
    }

    function wishlist_get()
    {

        $user_id = $this->get('user_id');

        $this->load->model('Retriever');
        $wishlist_items = $this->Retriever->get_wishlist($user_id);
        $this->response($wishlist_items, 200);
    }

    function producer_detail_get()
    {
        $producer_username = trim($this->get('producer_username'));

        $this->ci->load->model('tank_auth/users');
        $user_id_email = $this->ci->users->get_user_id_email($producer_username);
        $producer_profile = $this->ci->users->get_user_profile($user_id_email->id);
        $this->response($producer_profile);
    }

    function notifications_get()
    {
        $this->load->database();
        //$this->db->select('choice_producer, phone_no');
        //$this->db->where('user_type', 'consumer');
        $this->db->order_by("update_timestamp", "desc");
        $query = $this->db->get('system_notifications', 10);

       $result['not'] = $query->result();
       $result['count'] = $query->num_rows();
        $this->response($result);
    }
    
     function notifications_producer_get()
    {
         $userid = trim($this->get('userid'));
         
        $this->load->database();
        //$this->db->select('choice_producer, phone_no');
        //$this->db->where('user_type', 'consumer');
        $this->db->order_by("update_timestamp", "desc");
        $this->db->where('producer_id',$userid);
        $query = $this->db->get('system_notifications', 10);

       $result['not'] = $query->result();
       $result['count'] = $query->num_rows();
        $this->response($result);
    }

    function _create_field_filter()
    {
        $filterString = $this->get('fields');
//      $filterString = urldecode($filterString);
        if ($filterString != false)
        {
            $foodBankFields = json_decode($filterString, true);
            return $foodBankFields;
        } else
        {
            return false;
        }
    }

    function get_track_info_get()
    {

        $param['tag_id'] = $this->get('tag_id');

        $duration = $this->get('duration');
        $duration = (int) $duration;  //min
        //convert to seconds
        $duration = $duration * 60; //sec


        $current_time = strtotime($this->get('timestamp'));

        $time_limit = $current_time - $duration;

        $param['timestamp'] = date('Y-m-d H:i:s', $time_limit);

        $this->load->model('Retriever');
        $this->response($this->Retriever->get_tag_tracks($param), 200);
    }

}