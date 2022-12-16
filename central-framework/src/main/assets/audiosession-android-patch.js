(function (global, factory) {
    "use strict"
    // 以下逻辑主要用于判断环境

    if (typeof global === "undefined") {
        // 如果不是浏览器环境，则忽略后续逻辑
        return
    }

    if (typeof global.navigator.audioSession !== "undefined") {
        // 当前已经有 audioSession 对象，则忽略后续逻辑
        return
    }

    // 注入 Android WebView 的 AudioSession
    global.navigator.audioSession = factory(global)

}(window, function (window) {
    "use strict"
    // 以下逻辑主要用于生成 AudioSession 对象

    class AudioSession {
        // 该变量用于记录当前 WebView 正在播放的 Audio 控件对象
        // 这样 Android 就可以通过 this.webView.evaluateJavaScript("window.navigator.audioSession.play()"){} 的方式去暂停或继续播放音乐了
        audio;

        // 此函数用于向 Android 暴露接口，可以停止当前 WebView 正在播入的音乐
        pause() {
            if (this.audio) {
                this.audio.pause();
                // 如果置空了，则无法使用 play 继续播放音乐，相当于完全停止了音乐播放的功能
//                this.audio = null;
            }
        }

        // 此函数用于向 Android 暴露接口，可以控制当前 WebView 已暂停的音乐继续播放
        play() {
            if (this.audio) {
                this.audio.play();
            }
        }
    }

    let audioSession = new AudioSession();

    // 挟持 HTMLAudioElement 原来的 play 和 pause 方法，替换成我们的新 play、pause 方法
    // 在新的 play、pause 方法里通过 prompt 通知 Android 系统当前网页发生播放、暂停动作
    // Android WebView 应在 WebChromeClient#onJsPrompt 方法里面接收事件通知
    let origin_audio_play = HTMLAudioElement.prototype.play
    HTMLAudioElement.prototype._play = origin_audio_play
    HTMLAudioElement.prototype.play = function() {
        prompt("audio://play")
        audioSession.audio = this;

        if (!this._hacked) {
            this._hacked = true
            this.addEventListener('ended', function() {
                prompt("audio://onended")
            });

            this.addEventListener('pause', function() {
                prompt("audio://onpause")
            });
        }

        return this._play();
    }

    let origin_audio_pause = HTMLAudioElement.prototype.pause
    HTMLAudioElement.prototype._pause = origin_audio_pause
    HTMLAudioElement.prototype.pause = function() {
        prompt("audio://pause")
        audioSession.audio = this;
        return this._pause();
    }

    return audioSession;
}))