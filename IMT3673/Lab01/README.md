# Checklist

* [X] The git repository URL is correctly provided, such that command works: `git clone <url> `
* [X] When a user presses B1, activity A2 becomes active.
* [X] When a user presses B2, activity A3 becomes active.
* [X] When a user presses B3, activity A2 becomes active again.
* [X] When a user presses Back button from A2, she will go to A1.
* [X] When a user presses Back button from A1, it will quit the app.
* [X] When a user presses Back button from A3, it goes back to A2.
* [X] The field T2 shows "Hello " + the value of T1 from A1 (**Only if the value from T1 is not empty**)
* [X] The field T3 shows the same value "From A3: " if T4 was edited but Back button was pressed(**A bit unsure of what was supposed to happen here**)
* [X] The field T3 shows "From A3: " + the value of T4 from A3, if B3 was pressed and T4 was edited
* [x] The dropdown L1 field shows user-selected option when the app is restarted again (after being killed from running in the background)
