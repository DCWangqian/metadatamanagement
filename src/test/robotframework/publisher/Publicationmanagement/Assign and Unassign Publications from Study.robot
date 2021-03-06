*** Settings ***
Documentation     Test for assigning and unassigning publications to a publisher project and verify it under study tab
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Assigning and unassigning publications to a publisher project and assert it
    Mark and edit the publication tab of the project
    Assign a publication to the project
    Get back to german home page
    Click on study tab
    Click on the first study in the list of studies
    Assert the publication belongs to study selected study
    Unassign the publication from the project
    Get back to german home page
    Click on study tab
    Click on the first study in the list of studies
    Assert the publication has been unassigend from the selected study
    Get back to german home page
    click on cockpit button
    Switch To Settings Tab
    Click Publications Checkbox
    Get back to home page and deselect project

*** Keywords ***
Mark and edit the publication tab of the project
    Select project by name  robotprojectrelease4${BROWSER}
    click on cockpit button
    Switch To Settings Tab
    Click Publications Checkbox
    Switch To Status Tab
    Click on Publication Edit Button

Click on Publication Edit Button
   Click Element Through Tooltips    xpath=//md-card[@type="publications"]//button[@ng-disabled="ctrl.isProjectReleased()"]//span[text()="Bearbeiten"]

Assign a publication to the project
    Clear Element Text   xpath=//md-card//input[@type="search"]
    Input Text  xpath=//md-card//input[@type="search"]   The labour market's requirement profiles for higher education graduates
    Click Element Through Tooltips   xpath=//div//ul//li//span//span[contains(., "The labour market's requirement profiles for higher education graduates")]

Unassign the publication from the project
    Get back to german home page
    Select project by name  robotprojectrelease4${BROWSER}
    click on cockpit button
    Click on Publication Edit Button
    Delete the publication from the study

Click on the first study in the list of studies
    Click Element Through Tooltips   xpath=//a[@class='fdz-search-result'][1]

Assert the publication belongs to study selected study
    Wait Until Page Contains Element  xpath=//div//a[contains(.,"The labour market's requirement profiles for higher education graduates")]

Assert the publication has been unassigend from the selected study
    Wait Until Page Contains Element  xpath=//div[@ng-if="ctrl.counts.publicationsCount == 0"][contains(.,"Publikationen zu dieser Studie: Nicht erfasst.")]

Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Delete the publication from the study
    Click Element Through Tooltips    xpath=//button[@ng-click="ctrl.removePublication(publication)"]//md-icon[contains(.,"delete_forever")]
