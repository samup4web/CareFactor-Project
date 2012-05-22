Welcome to <?php echo $site_name; ?>,

Thanks for joining <?php echo $site_name; ?>. We listed your sign in details below. Make sure you keep them safe.
Follow this link to login on the site:

<?php $this->load->helper('url'); echo site_url('/auth/login/'); ?>

<?php if (strlen($username) > 0) { ?>

Your username: <?php echo $username; ?>
<?php } ?>

Your email address: <?php echo $email; ?>

<?php /* Your password: <?php echo $password; ?>

*/ ?>

Have fun!
The <?php echo $site_name; ?> Team