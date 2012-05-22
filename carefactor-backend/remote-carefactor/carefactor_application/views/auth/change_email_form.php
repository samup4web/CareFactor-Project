
<div style="font-size: 14px; font-family: sans-serif;  padding: 5px 5px 5px 5px;  color: #346a2a; ">
    
    <img src="<?php echo base_url() . "images/home_logo.jpg"; ?>" /><br><br>

Welcome to CareFactor! <?php echo anchor('https://www.facebook.com/pages/CareFactor/281976968505841', 'Visit our Facebook page'); ?> <br> <br>

Link to Our page on Ericsson Application Awards Website! <?php echo anchor('http://www.ericssonapplicationawards.com/team/profile/carefactor', 'here'); ?> <br> <br>
<span>
A solution to help ease challenges with food waste and logistics. Our application aims to decrease food waste by optimizing the distribution channels which is an essential part of supply chain management. 

The Team CareFactor takes pleasure in using technology to support a solution to an important sustainability problem. </span>

<br> <br>  

</div>

<?php
$password = array(
	'name'	=> 'password',
	'id'	=> 'password',
	'size'	=> 30,
);
$email = array(
	'name'	=> 'email',
	'id'	=> 'email',
	'value'	=> set_value('email'),
	'maxlength'	=> 80,
	'size'	=> 30,
);
?>
<?php echo form_open($this->uri->uri_string()); ?>
<table>
	<tr>
		<td><?php echo form_label('Password', $password['id']); ?></td>
		<td><?php echo form_password($password); ?></td>
		<td style="color: red;"><?php echo form_error($password['name']); ?><?php echo isset($errors[$password['name']])?$errors[$password['name']]:''; ?></td>
	</tr>
	<tr>
		<td><?php echo form_label('New email address', $email['id']); ?></td>
		<td><?php echo form_input($email); ?></td>
		<td style="color: red;"><?php echo form_error($email['name']); ?><?php echo isset($errors[$email['name']])?$errors[$email['name']]:''; ?></td>
	</tr>
</table>
<?php echo form_submit('change', 'Send confirmation email'); ?>
<?php echo form_close(); ?>