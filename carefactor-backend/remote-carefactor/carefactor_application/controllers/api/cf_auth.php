<?php

defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * request
 *
 * This is an example of a few basic user interaction methods you could use
 * all done with a hardcoded array.
 *
 * @package	CodeIgniter
 * @subpackage	Rest Server
 * @category	Controller
 * @Author Samup4web

 */
// This can be removed if you use __autoload() in config.php OR use Modular Extensions


require APPPATH . '/libraries/REST_Controller.php';

class Cf_auth extends REST_Controller {

    function __construct()
    {
        parent::__construct();
        $this->ci = & get_instance();
        $this->load->library('tank_auth');
        $this->lang->load('tank_auth');
    }

    /**
     * Sign in from mobile app.
     * 
     * var login - login name or email (user id)
     * var password - user password
     */
    function sign_in_get()
    {
        $login = $this->get('login');
        $password = $this->get('password');

        //login($login, $password, $remember, $login_by_username, $login_by_email)
        if ($this->tank_auth->login($login, $password, true, true, true))
        {
            $this->response(1, 200);
        } else
        {
            $this->response(0, 200);
        }
    }

    function update_user_profile_post()
    {
        $userdata['user_id'] = trim($this->post('user_id'));
        $userdata['lat'] = trim($this->post('lat'));
        $userdata['lon'] = trim($this->post('lon'));
        $userdata['address_line'] = trim($this->post('address_line'));
        $userdata['locality'] = trim($this->post('locality'));
        $userdata['country'] = trim($this->post('country'));
        $userdata['phone_no'] = trim($this->post('phone_no'));
        $userdata['is_notification_active'] = trim($this->post('is_notification_active'));
        $userdata['is_override_auto_location'] = trim($this->post('is_override_auto_location'));
        $userdata['web_site'] = trim($this->post('web_site'));
        $userdata['facebook_page_link'] = trim($this->post('facebook_page_link'));
        $userdata['other_contact_details'] = trim($this->post('other_contact_details'));
        $userdata['choice_producer'] = trim($this->post('choice_producer_list'));

     
        $this->load->model('Storer');
        // $this->response($userdata);
        $this->response($this->Storer->update_user_profile($userdata));
    }

    function fetch_user_info_get()
    {

        $username = $this->get('username');

        $this->ci->load->model('tank_auth/users');
        $user_id_email = $this->ci->users->get_user_id_email($username);


        $user_profile = $this->ci->users->get_user_profile($user_id_email->id);
        $user_profile = array_merge($this->object2array($user_id_email), $this->object2array($user_profile));

        $this->response($user_profile);
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

    /**
     * sign_up / register  a new account.
     * 
     * var username
     * var email
     * var user_type
     * var password
     * 
     */
    function sign_up_get()
    {
        $username = $this->get('username');
        $email = $this->get('email');
        $userInfo['user_type'] = $this->get('user_type');
        $password = $this->get('password');


        //create_user($username, $email, $password, $email_activation, $userInfo)
        if (!is_null($data = $this->tank_auth->create_user($username, $email, $password, false, $userInfo)))
        {
            //success

            try
            {
                $data['site_name'] = $this->config->item('website_name', 'tank_auth');
                $this->_send_email('welcome', $email, $data);
                
            } catch (Exception $e)
            {
                
            }

            $this->response(1);
            
        } else
        {
            $errors = $this->tank_auth->get_error_message();
            $this->response($errors);
//            foreach ($errors as $k => $v)
//                $data['errors'][$k] = $this->lang->line($v);
        }
    }

    function _send_email($type, $email, &$data)
    {
        $this->load->library('email');
        $this->email->from($this->config->item('webmaster_email', 'tank_auth'), $this->config->item('website_name', 'tank_auth'));
        $this->email->reply_to($this->config->item('webmaster_email', 'tank_auth'), $this->config->item('website_name', 'tank_auth'));
        $this->email->to($email);
        $this->email->subject(sprintf($this->lang->line('auth_subject_' . $type), $this->config->item('website_name', 'tank_auth')));
        $this->email->message($this->load->view('email/' . $type . '-html', $data, TRUE));
        $this->email->set_alt_message($this->load->view('email/' . $type . '-txt', $data, TRUE));
        $this->email->send();
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