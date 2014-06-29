Feature: As a user
  I want to search tv content currently on TV
  to decide what to watch

  Scenario: Search content
    Given A user
    When Do the search
    Then I get the current content on TV
