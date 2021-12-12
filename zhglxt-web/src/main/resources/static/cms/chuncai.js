!function (t) {
    function e(i) {
        if (n[i]) return n[i].exports;
        var a = n[i] = {exports: {}, id: i, loaded: !1};
        return t[i].call(a.exports, a, a.exports, e), a.loaded = !0, a.exports
    }

    var n = {};
    return e.m = t, e.c = n, e.p = "", e(0)
}([function (t, e, n) {
    n(1);
    var i = n(10), a = n(11);
    i.chuncai = function (t) {
        var e = {
            zhglxt: {key: "123456", userid: "asdfwerwer"},
            menu: {
                key: "主人，您想做点什么呢？>ω<",
                "显示公告": function () {
                    this.dynamicSay(this.opt.words[4])
                },
                "在线时间": function () {
                    this.dynamicSay("春菜已经和主人共同度过了 " + Math.floor((+new Date - 1638703658000) / 864e5) + " 天了哦~，春菜是不是很棒呀>ω<")
                },
                "吃大餐啦": {
                    key: "哇喔~主人要请我吃点什么呢>ω<？",
                    "海鲜大餐": "嗷呜~，好好吃哦。多谢主人的款待>ω<",
                    "胡萝卜": "人家又不是小兔子 QwQ",
                    "秋刀鱼": "主人这是什么？呀！好长！诶？！好滑哦(๑• . •๑)！阿呜～",
                    "胖次": "哇~ 好可爱的胖次~~~",
                    "淡定红茶": "喝完了，ˊ_>ˋ和我签订契约，成为你的淡定少女吧QwQ",
                    "麻辣烫": "喝完啦，ˊ_>ˋ美味的麻辣烫，真好吃。么么哒~"
                },
                "传送大门": {
                    "GitEE": function () {
                        window.open("https://gitee.com/liuwy_dlsdys/zhglxt")
                    },
                    "GitHub": function () {
                        window.open("https://github.com/liuwy-dlsdys/zhglxt")
                    }
                },
            },
            syswords: ["主人您好~~ 欢迎回来╰(￣▽￣)╭", "主人，我们聊聊天吧 ヽ(✿ﾟ▽ﾟ)ノ", "说点什么好呢 oAo ?"],
            words: ["您为什么喜欢我呀？因为你像个小精灵~(灬ºωº灬)", "一起组团烧烤秋刀鱼", "白日依山尽，黄河入海流，欲穷千里目，更上 .. .. 一层楼?", "啦啦啦~今天吃点什么好呢？~", "据说，点赞的都找到女朋友啦╰(￣▽￣)╭","「不要啊」你以为我会这么说么，噗噗~"]
        };
        return t && i.extend(e.menu, t.more), new a(i.extend({}, e, t))
    }
}, function (t, e, n) {
    var i = n(2);
    "string" == typeof i && (i = [[t.id, i, ""]]);
    n(9)(i, {});
    i.locals && (t.exports = i.locals)
}, function (t, e, n) {
    e = t.exports = n(3)(), e.push([t.id, ".chuncai-main{left:90%;top:70%;display:block;position:fixed;z-index:100;width:85px;height:152px;overflow:visible;font-family:Microsoft Yahei,Helvetica,Arial,sans-serif}.chuncai-main>.chuncai-face{position:absolute;right:0;top:28px;width:85px;height:152px;cursor:grab;cursor:-webkit-grab}.chuncai-main>.chuncai-face:active{cursor:grabbing;cursor:-webkit-grabbing}.chuncai-main>.chuncai-face-00{background:url(" + n(4) + ") no-repeat}.chuncai-main>.chuncai-face-00>.chuncai-face-eye{background:url(" + n(5) + ") no-repeat;left:14px;top:49px;width:44px;height:19px;position:absolute;animation:ccblink 5s infinite}.chuncai-main>.chuncai-face-01{background:url(" + n(6) + ") no-repeat}.chuncai-main>.chuncai-face-02{background:url(" + n(7) + ') no-repeat}.chuncai-main>.chuncai-chat{position:absolute;left:-210px;top:28px;width:16em;border:1px solid #ff5a77;background:#ffe;font-size:12px;border-radius:4px}.chuncai-main>.chuncai-chat:after,.chuncai-main>.chuncai-chat:before{position:absolute;bottom:-4px;right:3px;border-bottom:5px solid transparent;border-right:14px solid #ffe;content:""}.chuncai-main>.chuncai-chat:before{bottom:-5px;right:2px;border-right:16px solid #ff5a77}.chuncai-main>.chuncai-chat>.chuncai-word{padding:.5em;color:gray;min-height:15px;word-wrap:break-word}.chuncai-main>.chuncai-chat>.chuncai-menu{display:none}.chuncai-main>.chuncai-chat>.chuncai-menu>a{cursor:pointer;display:inline-block;width:50%;text-align:center;color:#d2322d}.chuncai-main>.chuncai-chat>.chuncai-menu-btn{margin-top:.3em;padding:0 10px 2px;color:#ff5a77;font-family:monospace;text-align:right;cursor:pointer}.chuncai-main>.chuncai-input{width:240px;height:28px;border:1px;overflow:auto;position:absolute;display:none;left:-210px;top:0}.chuncai-main>.chuncai-input>.chucai-talk{float:left}.chuncai-main>.chuncai-input>.chucai-talkto{float:right;background:url(' + n(8) + ") no-repeat;cursor:pointer}a.chuncai-zhaohuan{display:none;position:fixed;bottom:0;right:0;margin:.5em .75em;padding:1px .7em;border:1px solid #ff5a77;color:#d2322d;background:#ffe;font-size:small;cursor:pointer;border-radius:3px}@keyframes ccblink{0%{opacity:0}79%{opacity:0}80%{opacity:1}to{opacity:0}}", ""])
}, function (t, e) {
    t.exports = function () {
        var t = [];
        return t.toString = function () {
            for (var t = [], e = 0; e < this.length; e++) {
                var n = this[e];
                n[2] ? t.push("@media " + n[2] + "{" + n[1] + "}") : t.push(n[1])
            }
            return t.join("")
        }, t.i = function (e, n) {
            "string" == typeof e && (e = [[null, e, ""]]);
            for (var i = {}, a = 0; a < this.length; a++) {
                var A = this[a][0];
                "number" == typeof A && (i[A] = !0)
            }
            for (a = 0; a < e.length; a++) {
                var o = e[a];
                "number" == typeof o[0] && i[o[0]] || (n && !o[2] ? o[2] = n : n && (o[2] = "(" + o[2] + ") and (" + n + ")"), t.push(o))
            }
        }, t
    }
}, function (t, e) {
    t.exports = "/zhglxt/cms/image/chuncai/png/face00.png"
}, function (t, e) {
    t.exports = "/zhglxt/cms/image/chuncai/png/face-eyes.png"
}, function (t, e) {
    t.exports = "/zhglxt/cms/image/chuncai/png/face01.png"
}, function (t, e) {
    t.exports = "/zhglxt/cms/image/chuncai/png/face02.png";
}, function (t, e) {
    t.exports = "/zhglxt/cms/image/chuncai/jpeg/ok.jpg"
}, function (t, e, n) {
    function i(t, e) {
        for (var n = 0; n < t.length; n++) {
            var i = t[n], a = f[i.id];
            if (a) {
                a.refs++;
                for (var A = 0; A < a.parts.length; A++) a.parts[A](i.parts[A]);
                for (; A < i.parts.length; A++) a.parts.push(u(i.parts[A], e))
            } else {
                for (var o = [], A = 0; A < i.parts.length; A++) o.push(u(i.parts[A], e));
                f[i.id] = {id: i.id, refs: 1, parts: o}
            }
        }
    }

    function a(t) {
        for (var e = [], n = {}, i = 0; i < t.length; i++) {
            var a = t[i], A = a[0], o = a[1], r = a[2], c = a[3], u = {css: o, media: r, sourceMap: c};
            n[A] ? n[A].parts.push(u) : e.push(n[A] = {id: A, parts: [u]})
        }
        return e
    }

    function A(t, e) {
        var n = v(), i = w[w.length - 1];
        if ("top" === t.insertAt) i ? i.nextSibling ? n.insertBefore(e, i.nextSibling) : n.appendChild(e) : n.insertBefore(e, n.firstChild), w.push(e); else {
            if ("bottom" !== t.insertAt) throw new Error("参数“insertAt”的值无效. 必须是 'top' 或者 'bottom'.");
            n.appendChild(e)
        }
    }

    function o(t) {
        t.parentNode.removeChild(t);
        var e = w.indexOf(t);
        e >= 0 && w.splice(e, 1)
    }

    function r(t) {
        var e = document.createElement("style");
        return e.type = "text/css", A(t, e), e
    }

    function c(t) {
        var e = document.createElement("link");
        return e.rel = "stylesheet", A(t, e), e
    }

    function u(t, e) {
        var n, i, a;
        if (e.singleton) {
            var A = b++;
            n = m || (m = r(e)), i = s.bind(null, n, A, !1), a = s.bind(null, n, A, !0)
        } else t.sourceMap && "function" == typeof URL && "function" == typeof URL.createObjectURL && "function" == typeof URL.revokeObjectURL && "function" == typeof Blob && "function" == typeof btoa ? (n = c(e), i = p.bind(null, n), a = function () {
            o(n), n.href && URL.revokeObjectURL(n.href)
        }) : (n = r(e), i = l.bind(null, n), a = function () {
            o(n)
        });
        return i(t), function (e) {
            if (e) {
                if (e.css === t.css && e.media === t.media && e.sourceMap === t.sourceMap) return;
                i(t = e)
            } else a()
        }
    }

    function s(t, e, n, i) {
        var a = n ? "" : i.css;
        if (t.styleSheet) t.styleSheet.cssText = y(e, a); else {
            var A = document.createTextNode(a), o = t.childNodes;
            o[e] && t.removeChild(o[e]), o.length ? t.insertBefore(A, o[e]) : t.appendChild(A)
        }
    }

    function l(t, e) {
        var n = e.css, i = e.media;
        if (i && t.setAttribute("media", i), t.styleSheet) t.styleSheet.cssText = n; else {
            for (; t.firstChild;) t.removeChild(t.firstChild);
            t.appendChild(document.createTextNode(n))
        }
    }

    function p(t, e) {
        var n = e.css, i = e.sourceMap;
        i && (n += "\n/*# sourceMappingURL=data:application/json;base64," + btoa(unescape(encodeURIComponent(JSON.stringify(i)))) + " */");
        var a = new Blob([n], {type: "text/css"}), A = t.href;
        t.href = URL.createObjectURL(a), A && URL.revokeObjectURL(A)
    }

    var f = {}, h = function (t) {
        var e;
        return function () {
            return "undefined" == typeof e && (e = t.apply(this, arguments)), e
        }
    }, d = h(function () {
        return /msie [6-9]\b/.test(self.navigator.userAgent.toLowerCase())
    }), v = h(function () {
        return document.head || document.getElementsByTagName("head")[0]
    }), m = null, b = 0, w = [];
    t.exports = function (t, e) {
        e = e || {}, "undefined" == typeof e.singleton && (e.singleton = d()), "undefined" == typeof e.insertAt && (e.insertAt = "bottom");
        var n = a(t);
        return i(n, e), function (t) {
            for (var A = [], o = 0; o < n.length; o++) {
                var r = n[o], c = f[r.id];
                c.refs--, A.push(c)
            }
            if (t) {
                var u = a(t);
                i(u, e)
            }
            for (var o = 0; o < A.length; o++) {
                var c = A[o];
                if (0 === c.refs) {
                    for (var s = 0; s < c.parts.length; s++) c.parts[s]();
                    delete f[c.id]
                }
            }
        }
    };
    var y = function () {
        var t = [];
        return function (e, n) {
            return t[e] = n, t.filter(Boolean).join("\n")
        }
    }()
}, function (t, e) {
    t.exports = jQuery
}, function (t, e, n) {
    function i(t, e) {
        if (!(t instanceof e)) throw new TypeError("不能将类作为函数调用")
    }

    var a = function () {
        function t(t, e) {
            for (var n = 0; n < e.length; n++) {
                var i = e[n];
                i.enumerable = i.enumerable || !1, i.configurable = !0, "value" in i && (i.writable = !0), Object.defineProperty(t, i.key, i)
            }
        }

        return function (e, n, i) {
            return n && t(e.prototype, n), i && t(e, i), e
        }
    }(), A = n(12), o = n(13), r = n(10), c = 0, u = 1, s = 2, l = 1, p = 0;
    r.fn.will = function (t, e) {
        return this.queue(e || "fx", function (e) {
            t.call(this), e()
        })
    };
    var f = function () {
        function t(e) {
            i(this, t), this.opt = e, this.init()
        }

        return a(t, [{
            key: "init", value: function () {
                this.fill(), this.postzhglxt(c), A(this.ele.children(".chuncai-face"), this.ele, this.savePos.bind(this)), o(this.ele.children(".chuncai-face"), this.ele, this.savePos.bind(this)), this.bindMenuEvents(), this.bindTalkEvents(), this.show()
            }
        }, {
            key: "postzhglxt", value: function (t) {
                var e = this;
                t === c ? (r.post("/zhglxt/cms/api", {
                    key: e.opt.zhglxt.key,
                    info: "讲个笑话吧",
                    userid: e.opt.zhglxt.userid
                }, function (t, n) {
                    "success" == n && (e.opt.words.push(t.text), e.opt.jokepos = e.opt.words.length - 1)
                }), r.post("/zhglxt/cms/api", {
                    key: e.opt.zhglxt.key,
                    info: "讲个故事吧",
                    userid: e.opt.zhglxt.userid
                }, function (t, n) {
                    "success" == n && (e.opt.words.push(t.text), e.opt.storypos = e.opt.words.length - 1)
                })) : t == u ? r.post("/zhglxt/cms/api", {
                    key: e.opt.zhglxt.key,
                    info: "讲个笑话吧",
                    userid: e.opt.zhglxt.userid
                }, function (t, n) {
                    "success" == n && (e.opt.words[e.opt.jokepos] = t.text)
                }) : t == s && r.post("/zhglxt/cms/api", {
                    key: e.opt.zhglxt.key,
                    info: "讲个故事吧",
                    userid: e.opt.zhglxt.userid
                }, function (t, n) {
                    "success" == n && (e.opt.words[e.opt.storypos] = t.text)
                })
            }
        }, {
            key: "fill", value: function () {
                r('<a class="chuncai-zhaohuan" href="javascript:;">召唤春菜</a>').appendTo(r("body"));
                var t = '\n<div class="chuncai-main">' +
                            '\n<div class="chuncai-face chuncai-face-00">' +
                                '\n<div class="chuncai-face-eye"></div>' +
                            '\n</div>' +

                            '\n<div class="chuncai-input">' +
                                '\n<input class="chuncai-talk" type="text" value="" />' +
                                '\n<input class="chuncai-talkto" type="button" value=" "/>' +
                            '\n</div>' +

                            '\n<div class="chuncai-chat">' +
                                '\n<div class="chuncai-word"></div>' +
                                '\n<div class="chuncai-menu"></div>' +
                                '\n<div class="chuncai-menu-btn">点我看看</div>' +
                            '\n</div>' +
                        '\n</div>';
                this.ele = r(t), r("body").append(this.ele)
            }
        }, {
            key: "show", value: function () {
                if (sessionStorage.chuncai) {
                    var t = JSON.parse(sessionStorage.chuncai);
                    this.ele.css({left: t.x, top: t.y})
                }
                var e = this;
                e.ele.find(".chuncai-word").empty(), this.ele.fadeIn().will(function () {
                    e.freeSay(1)
                }), r(".chuncai-zhaohuan").hide()
            }
        }, {
            key: "hide", value: function () {
                this.dynamicSay("记得叫我出来哦~"), this.ele.delay(1e3).fadeOut().will(function () {
                    r(".chuncai-zhaohuan").show()
                })
            }
        }, {
            key: "freeSay", value: function (t) {
                var e = this;
                clearInterval(this.freeSayTimer), t && (e.dynamicSay(e.opt.syswords[0]), e.ele.find(".chuncai-face").attr("class", "chuncai-face chuncai-face-0" + Math.floor(3 * Math.random()))), this.freeSayTimer = setInterval(function () {
                    if (e.allowswitchword) {
                        e.ele.find(".chuncai-menu").slideUp();
                        var t = Math.floor(Math.random() * e.opt.words.length);
                        e.dynamicSay(e.opt.words[t]), t == e.opt.jokepos ? e.postzhglxt(u) : t == e.opt.storypos && e.postzhglxt(s), e.ele.find(".chuncai-face").attr("class", "chuncai-face chuncai-face-0" + Math.floor(3 * Math.random()))
                    }
                }, 1e4)
            }
        }, {
            key: "bindMenuEvents", value: function () {
                var t = this, e = this.opt, n = t.ele.find(".chuncai-menu");
                t.ele.find(".chuncai-menu-btn").click(function () {
                    var i = (r(this), !!t.menuOn);
                    t.menuOn = !i, i ? t.closeMenu(1) : (t.fillMenu(e.menu), n.slideDown()), t.ele.find(".chuncai-input").slideUp()
                }), r(".chuncai-zhaohuan").click(function () {
                    t.show()
                })
            }
        }, {
            key: "bindTalkEvents", value: function () {
                var t = this, e = this.ele.find(".chuncai-talk");
                e.keypress(function (e) {
                    13 == e.keyCode && t.ele.find(".chuncai-talkto").click()
                }), this.ele.find(".chuncai-talkto").click(function () {
                    t.menuOn && t.closeMenu(p), r.post("/zhglxt/cms/api", {
                        key: t.opt.zhglxt.key,
                        info: e.val(),
                        userid: t.opt.zhglxt.userid
                    }, function (n, i) {
                        "success" == i && t.dynamicSay(n.text), e.val("")
                    })
                })
            }
        }, {
            key: "closeMenu", value: function (t, e) {
                this.ele.find(".chuncai-menu").slideUp(), this.menuOn = !1, e === l ? this.dynamicSay(this.opt.syswords[2]) : e === p || this.freeSay(t)
            }
        }, {
            key: "fillMenu", value: function (t) {
                clearInterval(this.freeSayTimer), clearTimeout(this.menuTimer);
                var e = this;
                this.menuTimer = setTimeout(function () {
                    e.menuOn && e.closeMenu(1, t)
                }, 1e4);
                var n = r.type(t), i = {
                    number: function () {
                        t == l && (this.dynamicSay(this.opt.syswords[1]), this.ele.find(".chuncai-input").slideDown())
                    }, string: function () {
                        this.dynamicSay(t), this.closeMenu()
                    }, function: function () {
                        t.call(this), this.closeMenu()
                    }, object: function () {
                        var e = this.ele.find(".chuncai-menu"), n = this;
                        e.slideUp().will(function () {
                            e.empty(), r.each(t, function (t, i) {
                                if ("key" == t) return n.dynamicSay(i), !0;
                                var a = r("<a>" + t + "</a>").click(function () {
                                    n.fillMenu(i)
                                });
                                e.append(a)
                            })
                        }).slideDown()
                    }
                };
                i[n] && i[n].call(this)
            }
        }, {
            key: "dynamicSay", value: function (t) {
                this.allowswitchword = !1, this.ele.stop(!0, !1);
                for (var e = this.ele.find(".chuncai-word"), n = this, i = (this.ele.find(".chuncai-input"), function (i, a) {
                    n.ele.will(function () {
                        e.html(t.substr(0, i + 1)), i == a - 1 && (n.allowswitchword = !0)
                    }).delay(100)
                }), a = 0, A = t.length; a < A; a++) i(a, A)
            }
        }, {
            key: "savePos", value: function (t, e) {
                clearTimeout(this.saveTimer), this.saveTimer = setTimeout(function () {
                    sessionStorage.chuncai = JSON.stringify({x: t, y: e})
                }, 2e3)
            }
        }]), t
    }();
    t.exports = f
}, function (t, e) {
    function n(t, e, n) {
        e || (e = t);
        var a, A;
        t.mousedown(function (t) {
            a = !0, A = i(t)
        }), $(document).mouseup(function () {
            a = !1
        }).mousemove(function (t) {
            if (a) {
                var o = i(t), r = {x: o.x - A.x, y: o.y - A.y};
                A = o, e.css({
                    left: ("+=" + r.x + "px").replace("+=-", "-="),
                    top: ("+=" + r.y + "px").replace("+=-", "-="),
                    position: "fixed"
                }), n && n(e.css("left"), e.css("top"))
            }
        })
    }

    function i(t) {
        var e = t || window.event;
        return {x: e.clientX, y: e.clientY}
    }

    t.exports = n
}, function (t, e) {
    function n(t, e, n) {
        e || (e = t);
        var i;
        t.on("touchstart", function (t) {
            i = t.originalEvent.touches[0]
        }), t.bind("touchmove", function (t) {
            t.preventDefault();
            var n = t.originalEvent.touches[0];
            e.css({left: n.pageX - 40, top: n.pageY - 70, position: "fixed"})
        })
    }

    t.exports = n
}]);