use std::{borrow::Cow, fmt::Debug};

use arboard::{Clipboard, ImageData};
use jni::{errors::Error, objects::{JByteArray, JObject}, sys::jobject, JNIEnv};

// Error

pub enum ImageError {
    JniError,
}

impl From<Error> for ImageError {
    fn from(_error: Error) -> Self {
        ImageError::JniError
    }
}

// ClipboardImage

pub struct ClipboardImage {
    pub width: usize,
    pub height: usize,
    pub data: Vec<u8>,
}

impl ClipboardImage {

    pub fn to_image_data(&self) -> ImageData<'_> {
        ImageData {
            width: self.width,
            height: self.height,
            bytes: Cow::Borrowed(&self.data),
        }
    }

    pub fn from_java(mut env: JNIEnv, obj: jobject) -> Result<Self, ImageError> {
        let object = unsafe { JObject::from_raw(obj) };

        let width = env.get_field(&object, "width", "I")?.i()? as usize;
        let height = env.get_field(&object, "height", "I")?.i()? as usize;

        let raw_data = env.get_field(&object, "data", "[B")?.as_jni();
        let java_array = unsafe { JByteArray::from_raw(raw_data.l) };

        let data = env.convert_byte_array(java_array)?;
        Ok(ClipboardImage { width, height, data })
    }

}

impl<'a> From<ImageData<'a>> for ClipboardImage {
    fn from(image_data: ImageData) -> Self {
        ClipboardImage {
            width: image_data.width,
            height: image_data.height,
            data: image_data.bytes.into(),
        }
    }
}

impl<'a> From<ClipboardImage> for ImageData<'a> {
    fn from(image: ClipboardImage) -> Self {
        ImageData {
            width: image.width,
            height: image.height,
            bytes: Cow::Owned(image.data),
        }
    }
}

impl Debug for ClipboardImage {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("ClipboardImage")
            .field("width", &self.width)
            .field("height", &self.height)
            .field("data", &format!("[{} bytes]", self.data.len()))
            .finish()
    }
}

/// Return an Option<ClipboardImage> of the clipboard image, or None if it's empty. (represented as null in Java)
pub fn get_native_clipboard_image() -> Option<ClipboardImage> {
    let mut clipboard = Clipboard::new().unwrap();
    let image_data = clipboard.get_image().ok()?;

    Some(image_data.into())
}

/// Take a ClipboardImage input and set the clipboard contents to the given image. Return true if successful, false otherwise.
pub fn set_native_clipboard_image(input: ClipboardImage) -> bool {
    let mut clipboard = Clipboard::new().unwrap();
    let data = input.to_image_data();
    clipboard.set_image(data).is_ok()
}
