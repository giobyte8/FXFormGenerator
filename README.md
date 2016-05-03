# FXFormGenerator
Automatic form generation for JavaFX 8.
Create a form from your POJO instances, use it as is or customize it with through a powerful API.

## Features

#### Core features
 * Appropriate inputs for Java types:
    - String => TextInput (You can force to TextArea)
    - int => Spinner
    - double => Spinner
    - float => Spinner
    - boolean => CheckBox
    - Date => DatePicker
    - LocalDate => DatePicker
    - Object => (You can pass an observable list with possible options)
    - Other => (You can always pass a custom Node to use as editor for a given POJO property)
 * Exclude fields from be included into form.
 * Form validation through java validation API (Error labels into form).
 * Display the form as a Dialog or
 * Construct the form and return it as a Node to be included into your UI components.

#### Aesthetic
 * Assign custom text for each field label
 * Assign custom order for the form fields
 * Configure the inputs distribution layout (Single column | Multiple columns | Multiple (Fill width rows) columns)
 * Assign custom text for title, header and buttons text when form is showed as a Dialog
 
## TODOs
 * Support to customize/configure form through annotations in POJO
 * Support for custom layout definition
 * Support for custom field min/max width definition
 * Support to custom fields rows spacing and input groups spacing
 * Support to custom form main container padding/margin
 * Support for custom CSS over form elements
 
## Contributors
  * Giovanni Aguirre | [@DiganmeGiovanni](https://github.com/DiganmeGiovanni)
