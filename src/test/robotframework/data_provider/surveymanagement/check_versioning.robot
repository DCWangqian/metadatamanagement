*** Settings ***
Documentation  Test editing survey page and versioning
Resource  ../resources/login_resource.robot
Resource  ../resources/home_page_resource.robot
Resource  ../resources/search_resource.robot
Force Tags  chromeonly

*** Test Cases ***
Editing survey and check versioning
  Login as dataprovider
  Select project by name  fileuploadproject
  Click on surveys tab
  Click Survey Edit Button
  Input Text  name=titleDe  Test1337
  Sleep  2s
  Click Save Button
  Click Version Menu
  Page Should Contain  vor ein paar Sekunden
  Exit Version Menu
  Input Text  name=titleDe  Test
  Click Save Button
  [Teardown]  Get back to home page and logout


*** Keywords ***
Get back to home page and logout
  Get back to home page
  ${present}=  Run Keyword And Return Status    Page Should Contain  Sie haben ungespeicherte Änderungen.
  Run Keyword If    ${present} == 'True'   Click Element Through Tooltips  xpath=//button[contains(.,'Ja')]
  Click Element Through Tooltips  xpath=//button[@id='logout']

Click Save Button
  Click Element Through Tooltips  xpath=//button[@type='submit']

Click Version Menu
  Click Element Through Tooltips  xpath=//button[md-icon[text()='undo']]

Exit Version Menu
  Click Element Through Tooltips  xpath=//button[span[text()='Abbrechen']]
