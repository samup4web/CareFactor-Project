<?php

defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * retreiver
 *
 * This is an example of a few basic user interaction methods you could use
 * all done with a hardcoded array.
 *
 * @package		CodeIgniter
 * @category	Model
 * @author		leogs
 */
class Retriever extends CI_Model {

    private $data_table = 'cf_food_bank';

    function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    function apply_fields_filter($fields)
    {

        if (isset($fields))
        {
            $select = implode(",", $fields);
            if ($select != false)
            {
                $this->db->select($select);
                return true;
            }
        } else
        {
            // throw new WrongFilterException();
        }
    }

    function apply_producer_filter($producer)
    {

        if (isset($producer))
        {
            $this->db->where('producer_id', $producer);
        } else
        {
            //throw new WrongFilterException();
        }
    }

    function apply_category_filter($category)
    {

        if (isset($category))
        {
            $this->db->where('category', $category);
        } else
        {
            //throw new WrongFilterException();
        }
    }

    function apply_region_filter($producer_4_region)
    {

        if (isset($producer_4_region))
        {
            for ($index = 0; $index < count($producer_4_region); $index++)
            {
                $this->db->where('user_id', $producer_4_region[$index]['user_id']);
            }
        } else
        {
            //throw new WrongFilterException();
        }
    }

    function get_foodbank_recent()
    {

//        $this->db->select('phone_no');
//        $this->db->where('locality', $region);
//        $this->db->where('is_notification_active', 'true');


        $query = $this->db->get($this->data_table);
        if ($query->num_rows() > 0)
        {
            $result['count'] = $query->num_rows();
            $result['foodbank'] = $query->result();
            return $result;
        } else
        {
            $result['count'] = 0;
            $result['foodbank'] = "[{data:null},{data:null}]";
            return $result;
        }
    }

    //($fields, $query_producer, $query_category, $query_region, $sort_expiry_date, $sort_producer, $sort_sumitted_date, $sort_proximity, $sort_proximity ););
    function get_foodbank_data($fields=NULL, $query_producer=NULL, $query_category=NULL, $query_region=NULL, $sort_expiry_date=NULL, $sort_producer=NULL, $sort_sumitted_date=NULL, $sort_proximity=NULL, $sort_proximity=NULL)
    {


        if ($query_region)
        {

            if ($query_region != "All Region")
            //$this->apply_region_filter($query_region);
                $this->db->select('user_id');
            $this->db->where('locality', $query_region);
            $this->db->where('user_type', 'producer');
//
            $query = $this->db->get('user_profiles');
//            
            $producer_4_region = $query->result_array();
//            
            //return $producer_4_region;
            $this->apply_region_filter($producer_4_region);
        }
        if ($query_producer)
        {
            if ($query_producer != "All Producer")
                $this->apply_producer_filter($query_producer);
        }
        if ($query_category)
        {
            if ($query_category != "All Categories")
                $this->apply_category_filter($query_category);
        }

        if ($fields)
        {
            $this->apply_fields_filter($fields);
        }

        $query = $this->db->from($this->data_table)
                //->where($this->sensor_measurement . '.sensor_id', $sensor_id)
                // ->order_by($this->sensor_measurement . '.timestamp', $order)
                ->get();
        if ($query->num_rows() > 0)
        {
            $result['count'] = $query->num_rows();
            $result['foodbank'] = $query->result();
            return $result;
        } else
        {
            $result['count'] = 0;
            $result['foodbank'] = "[{data:null},{data:null}]";
            return $result;
        }
    }

    function get_wishlist($user_id)
    {

//        $this->db->select('wish_list.wishlist_food_id, wish_list.date_booked, wish_list.wishlist_quantity
//            cf_food_bank.producer_id, cf_food_bank.user_id, cf_food_bank.category, 
//            cf_food_bank.foodname, cf_food_bank.quantity, cf_food_bank.unit, cf_food_bank. description, 
//            cf_food_bank.expiry_date');
        $this->db->where('wish_list.wishlist_user_id', $user_id);
        $this->db->from('wish_list', 'cf_food_bank');
        $this->db->join('cf_food_bank', 'wish_list.wishlist_food_id = cf_food_bank.id');
        $this->db->order_by('wish_list.wishlist_id', 'asc');
        
        $query = $this->db->get();

        if ($query->num_rows() > 0)
        {
            $result['count'] = $query->num_rows();
            $result['wishlist'] = $query->result();
            return $result;
        } else
        {
            $result['count'] = 0;
            $result['wishlist'] = "[{data:null},{data:null}]";
            return $result;
        }
    }

}
