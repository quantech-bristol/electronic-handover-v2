# TODO LIST

Before Friday:
- Fix whitelabel error page to show generic error page
- add link to admin stuff for doctor/admin users <- implemented by nuha but appears to be a bug in the code concerning user creation.
- Internationalization file
- Display form error messages or each entry
- Ability to delete patient from your handovers, given discharge etc.
- Any UI changes
- Notes visable on handovers
- Add patient text doesn't fit inside the button
- Change the ---> arrow added to handovers to nicer symbol or something?
- Sent handover link only appears on viewPatients

Testing:
- Unit test all services made so far.
- Figure out how to carry out unit tests using URLs.
- Research white-box testing.

Handovers:
- Work out where to store completed handovers
- Handover input error handling for getters and setters (e.g. cannot create a new handover for a patient that is currently already part of one)
- Finalise what attributes Handover should have.
- Long term storage of handovers?

Doctors:
- User team functionality (e.g. "view team" in controller)

Admin:
- Team admin notified when a patient is discharged?

Patients:
- Are patients meant to be able to be filtered by team?
- Patient input error handling for getters and setters
- Finalise what attributes Patient should have
- Sort date format, currently needs inputting 1998/01/13 instead of 13/01/1998
- Adjust formatting on add patient as recommendations touches input box
- Long term storage of patients details?

Teams.
- Team input error handling for getters and setters 
- Finalise what attributes Team should have
- Ward input error handling for getters and setters
- Make teams departments?

Departments.
- Consider structure of hospital
- possible need for multiple admins per department to handle issues if not on shift?
- Implement (likely) tree structure.

Wards:
- Finalise what attributes Ward should have

Users:
- Add Validation to data coming from forms [FIXED USERS]

Security/Authentication:
- (Future) Set security to hash passwords (plain atm for ease of sec debug)
- Sort out HTTPS

View/Cosmetic Changes:
- Sort red/blue lines on the dropdown of viewing patients
- Functionality to print out a particular patients details
- Change date inputs to date month year drop downs
- When a doctor sends a handover, set it so that the originating doctor is actually automatically set to the one who is creating it.
- Enable the "view all patients" button on the sidebar to actually view just the doctor's own patients.
- Maybe find a way to remember whether sidebar is shown or hidden when changing pages
- Add pending and sent handovers to UI.
- Maybe come up with a good dashboard layout for the "Overview" tab (probably optional at this stage)

