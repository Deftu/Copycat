use arboard::Clipboard;

/// Return an Option<String> of the clipboard contents, or None if it's empty. (represented as null in Java)
pub fn get_native_clipboard_string() -> Option<String> {
    let mut clipboard = Clipboard::new().unwrap();
    clipboard.get_text().ok()
}

/// Take a String input and set the clipboard contents to the given string. Return true if successful, false otherwise.
pub fn set_native_clipboard_string(input: String) -> bool {
    let mut clipboard = Clipboard::new().unwrap();
    clipboard.set_text(input).is_ok()
}
