<?php

defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Receive
 *
 * This is an example of a few basic user interaction methods you could use
 * all done with a hardcoded array.
 *
 * @package		CodeIgniter
 * @subpackage	Rest Server
 * @category	Controller
 * @author	Samup4web
 */
// This can be removed if you use __autoload() in config.php OR use Modular Extensions
require APPPATH . '/libraries/REST_Controller.php';

class Submit extends REST_Controller {

    function __construct()
    {
        parent::__construct();
        $this->ci = & get_instance();
        $this->load->library('tank_auth');
        $this->lang->load('tank_auth');
        $this->load->library('checker');
        $this->load->library('decoder');
    }

    function signin_post()
    {
        
    }

    function signup_post()
    {
        
    }

    function add_to_foodbank_post()
    {


        $newFoodBankItem['category'] = trim($this->post('category'));
        $newFoodBankItem['foodname'] = trim($this->post('foodname'));
        $newFoodBankItem['description'] = trim($this->post('description'));
        $newFoodBankItem['expiry_date'] = (trim($this->post('expiryDate')));
        $newFoodBankItem['pick_up_date_start'] = trim($this->post('availableDate1'));
        $newFoodBankItem['pick_up_date_end'] = trim($this->post('availableDate2'));
        $newFoodBankItem['is_free'] = strtoupper(trim($this->post('isFree')));
        $newFoodBankItem['price'] = trim($this->post('price'));
        if ($newFoodBankItem['is_free'] == "FALSE")
        {
            $newFoodBankItem['currency'] = trim($this->post('currency'));
        } else
        {
            $newFoodBankItem['currency'] = "-";
        }
        $newFoodBankItem['quantity'] = (int) trim($this->post('quantity'));
        $newFoodBankItem['producer_id'] = trim($this->post('producer'));
        $newFoodBankItem['user_id'] = trim($this->post('user_id'));
        $newFoodBankItem['unit'] = trim($this->post('units'));


        //var_dump($newFoodBankItem);


        $notification['not_heading'] = $newFoodBankItem['producer_id'] . ":" . $newFoodBankItem['foodname'] . " available";
        $notification['not_body'] = $newFoodBankItem['description'];
        $notification['not_type'] = "food_update";
        $notification['producer_id'] = $newFoodBankItem['user_id'];

        $this->load->model('Storer');
        $this->Storer->update_notification($notification);



        //
        $message['title'] = "New " . $newFoodBankItem['foodname'] . " available";
        $message['body'] = $newFoodBankItem['description'];
        $message['sender_id'] = $newFoodBankItem['user_id'];
        $message['recipient'] = "new_update";
        $message['producer_name'] = $newFoodBankItem['producer_id'];



        try
        {
            $this->_pushNotification($message);
        } catch (Exception $e)
        {
            
        }

        //var_dump($newFoodBankItem);
        //$this->load->model('Storer');


        $this->response($this->Storer->store_food_resource($newFoodBankItem));
    }

    function add_to_wish_list_post()
    {



        $newWishItem['wishlist_food_id'] = trim($this->post('food_id'));
        $newWishItem['wishlist_user_id'] = trim($this->post('user_id'));
        $newWishItem['wishlist_quantity'] = trim($this->post('unit'));
        $newWishItem['wishlist_state'] = trim($this->post('status'));

        //var_dump($newFoodBankItem);
        $this->load->model('Storer');
        $this->response($this->Storer->store_wish_list($newWishItem));
    }

    function remove_from_wishlist_post()
    {

        $wishlist_id = trim($this->post('wishlist_id'));

        $this->load->database();
        $this->db->delete('wish_list', array('wishlist_id' => $wishlist_id));
    }

    function push_advert_post()
    {

        $message['title'] = trim($this->post('title'));
        $message['body'] = trim($this->post('body'));
        $message['sender_id'] = trim($this->post('producer_id'));
        $message['recipient'] = trim($this->post('recipient'));



        $notification['not_heading'] = $message['title'];
        $notification['not_body'] = $message['body'];
        $notification['not_type'] = "advert";
        $notification['producer_id'] = $message['sender_id'];

        $this->load->model('Storer');
        $this->Storer->update_notification($notification);

        $this->_pushNotification($message);
    }

    function _pushNotification($message)
    {

        $notificationMessage = $message['title'] . '#' . $message['body'];
        $developerKey = "2932d576dc003becae4cb10a7ab53d3d";
        $applicationName = "CareFactor";
        $serviceId = "90c6684b866600613da5141593163c8e";

        $recipients_temp = $this->_setupRecipient($message);


        if ($recipients_temp == NULL)
            return;

        $recipients = array();

        if ($message['recipient'] == "new_update")
        {
            // if ($recipients_temp != null)
            foreach ($recipients_temp as $key => $value)
            {
                array_push($recipients, $value);
            }
        } else
        {
            // if ($recipients_temp != null)
            foreach ($recipients_temp as $key => $value)
            {
                foreach (array_values($value) as $key => $value)
                {
                    array_push($recipients, ($value));
                }
            }
        }


        //print_r($recipients);


        $server = "http://mp.labs.ericsson.net/mp/rpc";


        $request = xmlrpc_encode_request("startTextSession", array($serviceId, $notificationMessage, $recipients, $developerKey));

       


        $context = stream_context_create(array('http' => array(
                'method' => "POST",
                'header' => "Content-Type: text/xml\r\nUser-Agent: PHPRPC/1.0\r\nHost: rpc.CareFactor.com\r\n",
                'content' => $request
                )));


        $file = file_get_contents($server, false, $context);
        $response = xmlrpc_decode($file);
        if (is_array($response) and xmlrpc_is_fault($response))
        {
            echo "error";
        } else
        {
            var_dump($response);
        }
    }

    function _setupRecipient($message)
    {

        $this->ci->load->model('tank_auth/users');
        $user_profile = $this->_fetch_user_info($message['sender_id']);

        $recipient_array = array();

        if ($message['recipient'] == "region")
        {
            echo "\n region \n";
            $recipient_array = $user_profile = $this->ci->users->get_phoneNo_via_region($user_profile['locality']);
        } else if ($message['recipient'] == "country")
        {
            echo "\n country \n";
            $recipient_array = $user_profile = $this->ci->users->get_phoneNo_via_country($user_profile['country']);
        } else if ($message['recipient'] == "new_update")
        {

            $this->load->database();
            $this->db->select('choice_producer, phone_no');
            $this->db->where('user_type', 'consumer');
            $query = $this->db->get('user_profiles');
            if ($query->num_rows() > 0)
            {
                //iterate through and check if message['sender'] is in choice_producer
                foreach ($query->result() as $row)
                {


                    $pos = stripos($row->choice_producer, $message['producer_name']);

                    if ($pos === false)
                    {
                        // string needle NOT found in haystack                        
                    } else
                    {
                        // string needle found in haystack
                        if (trim($row->phone_no) == "")
                        {
                            array_push($recipient_array, "+4670647929");
                        } else
                        {
                            array_push($recipient_array, $row->phone_no);
                        }
                    }
                }
            }
        }

        if ($recipient_array != NULL)
            return $this->object2array($recipient_array);
    }

    function _fetch_user_info($user_id)
    {
        $this->ci->load->model('tank_auth/users');
        $user_profile = $this->ci->users->get_user_profile("$user_id");
        $user_profile = $this->object2array($user_profile);

        //var_dump($user_profile);

        return $user_profile;
    }

    function object2array($object)
    {
        if (is_object($object))
        {
            foreach ($object as $key => $value)
            {
                $array[$key] = $value;
            }
        } else
        {
            $array = $object;
        }
        return $array;
    }

}

?>