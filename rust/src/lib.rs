mod images;
mod strings;

use arboard::Clipboard;
use jni::{
    objects::{JClass, JString, JValue},
    sys::{jboolean, jobject, jstring},
    JNIEnv,
};

#[no_mangle]
pub extern "system" fn Java_dev_deftu_clipboard_ClipboardNative_getString<'a>(
    env: JNIEnv<'a>,
    _class: JClass<'a>,
) -> jstring {
    let output = strings::get_native_clipboard_string();

    match output {
        Some(string) => env
            .new_string(string)
            .expect("Couldn't create Java string!")
            .into_raw(),
        None => std::ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_dev_deftu_clipboard_ClipboardNative_setString<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
    input: JString<'a>,
) -> jboolean {
    let input = match env.get_string(&input) {
        Ok(input) => input.into(),
        Err(_) => {
            let mut clipboard = Clipboard::new().unwrap();
            clipboard.clear().unwrap();

            return false as jboolean;
        }
    };

    strings::set_native_clipboard_string(input) as jboolean
}

#[no_mangle]
pub extern "system" fn Java_dev_deftu_clipboard_ClipboardNative_getImage<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
) -> jobject {
    let output = images::get_native_clipboard_image();

    match output {
        Some(image) => {
            let image_data = unsafe {
                let (addr, len) = {
                    let raw = image.data.leak();
                    (raw.as_mut_ptr(), raw.len())
                };

                env.new_direct_byte_buffer(addr, len)
                    .expect("Couldn't create Java byte buffer!")
            };

            let image = env
                .new_object(
                    "dev/deftu/clipboard/ClipboardImage",
                    "(IILjava/nio/ByteBuffer;)V",
                    &[(image.width as i32).into(), (image.height as i32).into(), JValue::Object(&image_data)],
                )
                .expect("Couldn't create ClipboardImage object!");
            image.into_raw()
        }
        None => std::ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_dev_deftu_clipboard_ClipboardNative_setImage<'a>(
    env: JNIEnv<'a>,
    _class: JClass<'a>,
    input: jobject,
) -> jboolean {
    let image = match images::ClipboardImage::from_java(env, input) {
        Ok(image) => image,
        Err(_) => {
            let mut clipboard = Clipboard::new().unwrap();
            clipboard.clear().unwrap();

            return false as jboolean;
        }
    };

    images::set_native_clipboard_image(image) as jboolean
}

#[no_mangle]
pub extern "system" fn Java_dev_deftu_clipboard_ClipboardNative_clear<'a>(
    _env: JNIEnv<'a>,
    _class: JClass<'a>,
) -> jboolean {
    let mut clipboard = Clipboard::new().unwrap();
    clipboard.clear().is_ok() as jboolean
}
