<?php

defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Submit_test
 *
 * This is an example of a few basic user interaction methods you could use
 * all done with a hardcoded array.
 * 
 * @package		CodeIgniter
 * @subpackage	unit testing
 * @category	Controller
 * @author		Samuel Idowu
 */
require APPPATH . '/libraries/Unit_test_Controller.php';

class Main extends CI_Controller {

    function __construct()
    {
        parent::__construct();
    }

    function pushServer()
    {

        $developerKey = "2932d576dc003becae4cb10a7ab53d3d";
        $applicationName = "CareFactor";

        $request = xmlrpc_encode_request("getServiceList", array($applicationName, $developerKey));

        $context = stream_context_create(array('http' => array(
                'method' => "POST",
                'header' => "Content-Type: text/xml\r\nUser-Agent: PHPRPC/1.0\r\nHost: rpc.CareFactor.com\r\n",
                'content' => $request
                )));

        $server = "http://mp.labs.ericsson.net/mp/rpc";
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

    function pushNotification()
    {
     
        $notificationMessage = "New request by consumer!";
        
        $developerKey = "2932d576dc003becae4cb10a7ab53d3d";
        $applicationName = "CareFactor";
        $serviceId = "90c6684b866600613da5141593163c8e";
        
        $recipients= array("+46730647929");
        
         $server = "http://mp.labs.ericsson.net/mp/rpc";

        $request = xmlrpc_encode_request("startTextSession", 
                array($serviceId, $notificationMessage, $recipients, $developerKey));
        
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

}
