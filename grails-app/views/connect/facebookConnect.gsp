<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h3>Connect to Facebook</h3>

    <form action="/connect/facebook" method="POST">
        <input type="hidden" name="scope" value="email, public_profile, user_about_me, user_location, user_hometown, user_birthday, offline_access" />
        <div class="formInfo">
            <p>You aren't connected to Facebook yet. Click the button to connect this application with your Facebook account.</p>
        </div>
        <p><button type="submit">Connect to Facebook</button></p>
    </form>
</body>
</html>