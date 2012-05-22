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

    private $sensor_measurement = 'gateway_feeds';

    function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    function drop_measurements()
    {
        $query = $this->db->from($this->sensor_measurement)
                ->order_by($this->sensor_measurement . '.timestamp', 'asc')
                ->get();
        $results['count'] = $query->num_rows();
        $results['measurements'] = $query->result();
        return $results;
    }

    function drop_measurements_period($after, $before)
    {
//    echo 'test';
        $query = $this->db->from($this->sensor_measurement)
                ->where($this->sensor_measurement . '.timestamp <=', $before)
                ->where($this->sensor_measurement . '.timestamp >=', $after)
                ->order_by($this->sensor_measurement . '.timestamp', 'asc')
                ->get();
//    echo $query->num_rows();
        $results['count'] = $query->num_rows();
        $results['measurements'] = $query->result();
        return $results;
    }

    function graph_data()
    {
        $query = $this->db->from($this->sensor_measurement)
                ->select('data_value,sensor_id,timestamp')
                ->order_by($this->sensor_measurement . '.timestamp', 'asc')
                ->limit(50)
                ->get();

        foreach ($query->result() as $row)
        {
            if ($row->data_value < 100)
            {
                $results['temperature'][] = $row->data_value;
                $results['sensor_id'][] = $row->sensor_id;
                $results['timestamp'][] = $row->timestamp;
            }
        }
        return $results;
    }

}