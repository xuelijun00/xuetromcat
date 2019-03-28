/** 
 @Name : jeDate v2.0 日期控件
 @Author: chne guojun
 @Date: 2015-12-28
 @QQ群：516754269
 @Site：https://github.com/singod/jeDate
 */
!
function(a) {
    var f, g, h, b = {},
    c = document,
    d = "#jedatebox",
    e = b.query = function() {
        function a(l, m) {
            var n, o, p, q, r, s;
            if (m = m || document, !/^[\w\-_#]+$/.test(l) && m.querySelectorAll) return b(m.querySelectorAll(l));
            if ( - 1 < l.indexOf(",")) {
                for (n = l.split(/,/g), o = [], p = 0, q = n.length; q > p; ++p) o = o.concat(a(n[p], m));
                return k(o)
            }
            if (n = l.match(e), o = n.pop(), q = (o.match(g) || i)[1], r = !q && (o.match(f) || i)[1], p = !q && (o.match(h) || i)[1], o = l.match(/\[(?:[\w\-_][^=]+)=(?:[\'\[\]\w\-_]+)\]/g), r && !o && !p && m.getElementsByClassName) p = b(m.getElementsByClassName(r));
            else {
                if (p = !q && b(m.getElementsByTagName(p || "*")), r && (p = d(p, "className", RegExp("(^|\\s)" + r + "(\\s|$)"))), q) return (n = m.getElementById(q)) ? [n] : [];
                if (o) for (q = 0; q < o.length; q++) r = (o[q].match(j) || i)[1],
                s = (o[q].match(j) || i)[2],
                s = s.replace(/\'/g, "").replace(/\-/g, "\\-").replace(/\[/g, "\\[").replace(/\]/g, "\\]"),
                p = d(p, r, RegExp("(^" + s + "$)"))
            }
            return n[0] && p[0] ? c(n, p) : p
        }
        function b(a) {
            try {
                return Array.prototype.slice.call(a)
            } catch(b) {
                for (var c = [], d = 0, e = a.length; e > d; ++d) c[d] = a[d];
                return c
            }
        }
        function c(a, b, d) {
            var o, p, q, j, k, l, m, n, e = a.pop();
            if (">" === e) return c(a, b, !0);
            for (j = [], k = -1, l = (e.match(g) || i)[1], m = !l && (e.match(f) || i)[1], e = !l && (e.match(h) || i)[1], n = -1, e = e && e.toLowerCase(); o = b[++n];) {
                p = o.parentNode;
                do
                if (q = (q = (q = !e || "*" === e || e === p.nodeName.toLowerCase()) && (!l || p.id === l)) && (!m || RegExp("(^|\\s)" + m + "(\\s|$)").test(p.className)), d || q) break;
                while (p = p.parentNode);
                q && (j[++k] = o)
            }
            return a[0] && j[0] ? c(a, j) : j
        }
        function d(a, b, c) {
            for (var e, d = -1,
            f = -1,
            g = []; e = a[++d];) c.test(e.getAttribute(b)) && (g[++f] = e);
            return g
        }
        var e = /(?:[\*\w\-\\.#]+)+(?:\[(?:[\w\-_][^=]+)=(?:[\'\[\]\w\-_]+)\])*|\*|>/gi,
        f = /^(?:[\w\-_]+)?\.([\w\-_]+)/,
        g = /^(?:[\w\-_]+)?#([\w\-_]+)/,
        h = /^([\w\*\-_]+)/,
        i = [null, null, null],
        j = /\[([\w\-_][^=]+)=([\'\[\]\w\-_]+)\]/,
        k = function() {
            var a = +new Date,
            b = function() {
                var b = 1;
                return function(c) {
                    var d = c[a],
                    e = b++;
                    return d ? !1 : (c[a] = e, !0)
                }
            } ();
            return function(c) {
                for (var h, d = c.length,
                e = [], f = -1, g = 0; d > g; ++g) h = c[g],
                b(h) && (e[++f] = h);
                return a += 1,
                e
            }
        } ();
        return a
    } ();
    b.each = function(a, b) {
        for (var c = 0,
        d = a.length; d > c && b(c, a[c]) !== !1; c++);
    },
    b.extend = function() {
        var d, a = function e(a, b) {
            for (var c in a) if (a.hasOwnProperty(c)) {
                if (a[c] instanceof Object && b[c] instanceof Object && e(a[c], b[c]), b.hasOwnProperty(c)) continue;
                b[c] = a[c]
            }
        },
        b = {},
        c = arguments;
        if (!c.length) return {};
        for (d = c.length - 1; d >= 0; d--) a(c[d], b);
        return c[0] = b,
        b
    },
    b.trim = function(a) {
        return a = a || "",
        a.replace(/^\s|\s$/g, "").replace(/\s+/g, " ")
    },
    b.attr = function(a, b, c) {
        return "string" == typeof b && "undefined" == typeof c ? a.getAttribute(b) : (a.setAttribute(b, c), this)
    },
    b.stopmp = function(b) {
        return b = b || a.event,
        b.stopPropagation ? b.stopPropagation() : b.cancelBubble = !0,
        this
    },
    b.getCss = function(a, b) {
        return a.currentStyle ? a.currentStyle[b] : window.getComputedStyle ? document.defaultView.getComputedStyle(a)[b] : null
    },
    b.hasClass = function(a, b) {
        return a = a || {},
        new RegExp("\\b" + b + "\\b").test(a.className)
    },
    b.addClass = function(a, c) {
        return a = a || {},
        b.hasClass(a, c) || (a.className += " " + c),
        a.className = b.trim(a.className),
        this
    },
    b.removeClass = function(a, c) {
        return a = a || {},
        b.hasClass(a, c) && (a.className = a.className.replace(new RegExp("(\\s|^)" + c + "(\\s|$)"), "")),
        this
    },
    b.on = function(a, b, c) {
        a && (a.addEventListener ? a.addEventListener(b, c, !1) : a.attachEvent ? a.attachEvent("on" + b, c) : a["on" + b] = c)
    },
    b.stopMosup = function(a, c) {
        "mouseup" !== a && b.on(c, "mouseup",
        function(a) {
            b.stopmp(a)
        })
    },
    b.html = function(a, b) {
        return "undefined" != typeof b || void 0 !== b && 1 === a.nodeType ? (a.innerHTML = b, this) : a.innerHTML
    },
    b.text = function(a, b) {
        if (void 0 === b || 1 !== a.nodeType) {
            var c = document.all ? a.innerText: a.textContent;
            return c
        }
        return document.all ? a.innerText = b: a.textContent = b,
        this
    },
    b.val = function(a, b) {
        return void 0 === b || 1 !== a.nodeType ? a.value: (a.value = b, this)
    },
    b.scroll = function(a) {
        return a = a ? "scrollLeft": "scrollTop",
        c.body[a] | c.documentElement[a]
    },
    b.winarea = function(a) {
        return c.documentElement[a ? "clientWidth": "clientHeight"]
    },
    b.parse = function(a, c, d) {
        a = a.concat(c);
        var d = d;
        return d.replace(/YYYY|MM|DD|hh|mm|ss/g,
        function() {
            return a.index = 0 | ++a.index,
            b.digit(a[a.index])
        })
    },
    b.nowDate = function(a, c) {
        var d = new Date(0 | a ?
        function(a) {
            return 864e5 > a ? +new Date + 864e5 * a: a
        } (parseInt(a)) : +new Date);
        return b.parse([d.getFullYear(), d.getMonth() + 1, d.getDate()], [d.getHours(), d.getMinutes(), d.getSeconds()], c)
    },
    b.montharr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
    b.isValHtml = function(a) {
        return /textarea|input/.test(a.tagName.toLocaleLowerCase())
    },
    b.weeks = ["日", "一", "二", "三", "四", "五", "六"],
    b.festival = function(a, b) {
        var c = "";
        switch (a) {
        case "01.01":
            c = "元旦";
            break;
        case "02.14":
            c = "情人";
            break;
        case "03.08":
            c = "妇女";
            break;
        case "04.05":
            c = "清明";
            break;
        case "05.01":
            c = "劳动";
            break;
        case "06.01":
            c = "儿童";
            break;
        case "08.01":
            c = "建军";
            break;
        case "09.10":
            c = "教师";
            break;
        case "10.01":
            c = "国庆";
            break;
        case "12.24":
            c = "平安";
            break;
        case "12.25":
            c = "圣诞";
            break;
        default:
            c = b
        }
        return c
    },
    b.digit = function(a) {
        return 10 > a ? "0" + (0 | a) : a
    },
    b.shdeCell = function(a) {
        e(d)[0].style.display = a ? "none": "block"
    },
    f = {
        dateCell: "#dateval",
        format: "YYYY-MM-DD hh:mm:ss",
        minDate: "1900-01-01 00:00:00",
        maxDate: "2099-12-31 23:59:59",
        isinitVal: !1,
        isTime: !1,
        isClear: !0,
        festival: !1,
        zIndex: 999,
        choosefun: function() {},
        clearfun: function() {},
        okfun: function() {}
    },
    g = function(a) {
        var c = this,
        d = JSON.parse(JSON.stringify(f));
        c.config = b.extend(d, a),
        c.init()
    },
    h = function(a) {
        return new g(a || {})
    },
    g.prototype = {
        init: function() {
            var h, i, k, n, a = this,
            f = a.config,
            g = e(f.dateCell)[0],
            j = window.event,
            l = c.createElement("div");
            e(d)[0] || (l.className = l.id = d.replace("#", ""), l.style.zIndex = f.zIndex, c.body.appendChild(l));
            try {
                k = j.target || j.srcElement || {}
            } catch(m) {
                k = {}
            }
            if (h = f.dateCell ? e(f.dateCell)[0] : k, n = b.nowDate(null, f.format), f.isinitVal && ("" == (b.val(g) || b.text(g)) ? b.isValHtml(g) ? b.val(g, n) : b.text(g, n) : b.isValHtml(g) ? b.val(g) : b.text(g)), j && k.tagName) {
                if (!h || h === b.elem) return;
                b.stopMosup(j.type, h),
                b.stopmp(j),
                a.setHtml(f, g)
            } else i = f.event || "click",
            b.each((h && h.length && h.length>0) ? h: [h],
            function(c, d) {
                b.stopMosup(i, a),
                b.on(d, i,
                function(c) {
                    b.stopmp(c),
                    d !== b.elem && a.setHtml(f, g)
                })
            })
        },
        setHtml: function(a, c) {
            var l, m, n, o, p, q, r, s, t, u, f = this,
            g = "",
            h = new Date,
            i = b.nowDate(null, a.format),
            j = "YYYY-MM" == a.format.match(/\w+|d+/g).join("-") ? !0 : !1,
            k = a.isinitVal ? b.isValHtml(c) ? b.val(c) : b.text(c) : "" == (b.val(c) || b.text(c)) ? i: b.isValHtml(c) ? b.val(c) : b.text(c);
            if (l = "" != b.val(c) || "" != b.text(c) ? k.match(/\d+/g) : [h.getFullYear(), h.getMonth() + 1, h.getDate(), h.getHours(), h.getMinutes(), h.getSeconds()], m = j ? '<div class="jedateym" style="width:100%;"><i class="prev triangle ymprev"></i><span class="jedateyy"><em class="jedateyearmonth"></em></span><i class="next triangle ymnext"></i></div>': '<div class="jedateym" style="width:50%;"><i class="prev triangle yearprev"></i><span class="jedateyy" data-ym="24"><em class="jedateyear"></em><em class="pndrop"></em></span><i class="next triangle yearnext"></i></div><div class="jedateym" style="width:50%;"><i class="prev triangle monthprev"></i><span class="jedatemm" data-ym="12"><em class="jedatemonth"></em><em class="pndrop"></em></span><i class="next triangle monthnext"></i></div>', n = '<div class="jedatetop">' + m + "</div>", o = j ? '<ul class="jedaym"></ul>': '<div class="jedatetopym" style="display: none;"><ul class="ymdropul"></ul><p><span class="jedateymchle">&#8592;</span><span class="jedateymchri">&#8594;</span><span class="jedateymchok">关闭</span></p></div>', p = '<ol class="jedaol"></ol><ul class="jedaul"></ul>', q = j ? '<div class="botflex jedatebtn"><span class="jedateclear" style="width:31%;">清空</span><span class="jedatetodaymonth" style="width:31%;">本月</span><span class="jedateok" style="width:31%;">确认</span></div>': '<ul class="botflex jedatehms"><li><em data-hms="24"></em><i>:</i></li><li><em data-hms="60"></em><i>:</i></li><li><em data-hms="60"></em></li></ul><div class="botflex jedatebtn"><span class="jedateclear" style="width:31%;">清空</span><span class="jedatetodaymonth" style="width:31%;">今天</span><span class="jedateok" style="width:31%;">确认</span></div>', r = '<div class="jedatebot">' + q + "</div>", s = j ? n + o + r: n + o + p + r + '<div class="jedateprophms"></div>', b.html(e(d)[0], s), a.isClear ? "": e(d + " .jedatebot .jedateclear")[0].style.display = "none", a.isTime ? (t = "" != b.val(c) || "" != b.text(c) ? [l[3], l[4], l[5]] : [h.getHours(), h.getMinutes() + 1, h.getSeconds()], b.each(e(d + " .jedatebot .jedatehms em"),
            function(a, c) {
                b.html(c, b.digit(t[a]))
            })) : (j || (e(d + " .jedatebot .jedatehms")[0].style.display = "none"), e(d + " .jedatebot .jedatebtn")[0].style.width = "100%"), j) b.html(e(d + " .jedaym")[0], f.onlyYMStr(l[0], l[1])),
            b.text(e(d + " .jedateym .jedateyearmonth")[0], l[0] + "年" + b.digit(l[1]) + "月"),
            f.onlyYMevents(f, a, c, l);
            else {
                for (u = 0; u < b.weeks.length; u++) g += '<li class="weeks" data-week="' + b.weeks[u] + '">' + b.weeks[u] + "</li>";
                b.html(e(d + " .jedaol")[0], g),
                f.getDateStr(l[0], l[1], l[2]),
                f.YearAndMonth(f, a, c, l)
            }
            b.shdeCell(!1),
            f.orien(e(d)[0], c),
            f.events(f, a, c, l)
        },
        onlyYMStr: function(a, c) {
            var d = "";
            return b.each(b.montharr,
            function(e, f) {
                d += "<li " + (c == f ? 'class="action"': "") + ' data-onym="' + a + "-" + b.digit(f) + '">' + a + "年" + b.digit(f) + "月</li>"
            }),
            d
        },
        onlyYMevents: function(a, c, f, g) {
            var h = e(d + " .jedateym .ymprev")[0],
            i = e(d + " .jedateym .ymnext")[0],
            j = parseInt(g[0]),
            k = parseInt(g[1]);
            b.each([h, i],
            function(i, l) {
                b.on(l, "click",
                function(i) {
                    b.stopmp(i);
                    var m = l == h ? j -= 1 : j += 1;
                    b.html(e(d + " .jedaym")[0], a.onlyYMStr(m, k)),
                    a.events(a, c, f, g)
                })
            })
        },
        orien: function(a, c, d) {
            var e, f = c.getBoundingClientRect();
            a.style.left = f.left + (d ? 0 : b.scroll(1)) + "px",
            e = f.bottom + a.offsetHeight / 1.5 <= b.winarea() ? f.bottom - 1 : f.top > a.offsetHeight / 1.5 ? f.top - a.offsetHeight + 1 : b.winarea() - a.offsetHeight,
            a.style.top = Math.max(e + (d ? 0 : b.scroll()) + 1, 1) + "px"
        },
        getDateStr: function(a, c, f) {
            function W(a, b) {
                var c = 0 == a % 4 && 0 != a % 100 || 0 == a % 400 ? 29 : 28;
                return [31, c, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][b - 1]
            }
            function X(a, b) {
                return parseInt(new Date(a, b - 1, 0).getDate())
            }
            var j, k, m, n, o, p, r, s, t, u, w, x, y, z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, Q, R, S, T, U, V, g = this,
            h = g.config,
            i = "";
            for (c = b.digit(c), b.text(e(d + " .jedateyear")[0], a + "年").attr(e(d + " .jedateyear")[0], "data-year", a), b.text(e(d + " .jedatemonth")[0], c + "月").attr(e(d + " .jedatemonth")[0], "data-month", c), j = function(a, c) {
                return h.festival ? b.festival(a, c) : c
            },
            k = function(a) {
                var b = a.split(" ");
                return b[0].split("-")
            },
            W(a, c), m = new Date(a, parseInt(c) - 1, 1).getDay(), n = 0 != m ? m: m + 7, o = X(a, c), p = X(a, parseInt(c) + 1), r = 1, s = k(h.minDate), t = k(h.maxDate), u = p, new Date(a, c, f), w = new Date(a, c, 1), x = new Date(a, c, p), y = new Date(s[0], s[1], s[2]), z = new Date(t[0], t[1], t[2]), A = y.getDate(), y > x ? r = parseInt(p) + 1 : y >= w && x >= y && (r = A), z && (B = z.getDate(), w > z ? u = r: z >= w && x >= z && (u = B)), C = n - 1; C >= 0; C--) G = b.digit(o - C),
            1 == c ? (D = parseInt(a) - 1, E = 13) : (D = a, E = c),
            H = parseInt(D.toString() + b.digit(parseInt(E) - 1).toString() + G.toString()),
            I = parseInt(s[0].toString() + b.digit(s[1]).toString() + b.digit(s[2]).toString()),
            J = parseInt(t[0].toString() + b.digit(t[1]).toString() + b.digit(t[2]).toString()),
            F = H >= I && J >= H ? "prevdate": F = "disabled",
            i += "<li class='" + F + "' data-y='" + D + "' data-m='" + (parseInt(E) - 1) + "' data-d='" + G + "'>" + j(parseInt(E) - 1 + "." + G, G) + "</li>";
            for (K = 1; r > K; K++) K = b.digit(K),
            i += '<li class="disabled" data-y="' + a + '" data-m="' + c + '" data-d="' + K + '">' + j(c + "." + K, K) + "</li>";
            for (L = r; u >= L; L++) M = "",
            L = b.digit(L),
            f == L && (M = "action"),
            i += '<li class="' + M + '" data-y="' + a + '" data-m="' + c + '" data-d="' + L + '">' + j(c + "." + L, L) + "</li>";
            for (N = u + 1; p >= N; N++) N = b.digit(N),
            i += '<li class="disabled" data-y="' + a + '" data-m="' + c + '" data-d="' + N + '">' + j(c + "." + N, N) + "</li>";
            for (P = 42 - n - W(a, c), Q = 1; P >= Q; Q++) Q = b.digit(Q),
            c >= 12 ? (R = parseInt(a) + 1, S = 0) : (R = a, S = c),
            U = parseInt(R.toString() + b.digit(parseInt(S) + 1).toString() + b.digit(Q).toString()),
            V = parseInt(s[0].toString() + b.digit(s[1]).toString() + b.digit(s[2]).toString()),
            J = parseInt(t[0].toString() + b.digit(t[1]).toString() + b.digit(t[2]).toString()),
            T = J >= U && U >= V ? "nextdate": T = "disabled",
            i += "<li class='" + T + "' data-y='" + R + "' data-m='" + (parseInt(S) + 1) + "' data-d='" + Q + "'>" + j(parseInt(S) + 1 + "." + Q, Q) + "</li>";
            b.html(e(d + " .jedaul")[0], i),
            b.attr(e(d + " .monthprev")[0], "data-y", b.digit(parseInt(c) - 1)),
            b.attr(e(d + " .monthnext")[0], "data-y", b.digit(parseInt(c) + 1))
        },
        events: function(a, c, f, g) {
            var h = e(d + " .yearprev")[0],
            i = e(d + " .yearnext")[0],
            j = e(d + " .monthprev")[0],
            k = e(d + " .monthnext")[0],
            l = new Date,
            m = e(d + " .jedateyear")[0],
            n = e(d + " .jedatemonth")[0],
            o = "YYYY-MM" == c.format.match(/\w+|d+/g).join("-") ? !0 : !1;
            o ? (b.each(e(d + " .jedaym li"),
            function(a, g) {
                b.on(g, "click",
                function(a) {
                    var h, i;
                    b.stopmp(a),
                    h = b.attr(g, "data-onym").match(/\w+|d+/g),
                    i = b.parse([h[0], h[1], 1], [0, 0, 0], c.format),
                    b.isValHtml(f) ? b.val(f, i) : b.text(f, i),
                    b.html(e(d)[0], ""),
                    b.shdeCell(!0)
                })
            }), b.on(e(d + " .jedatebot .jedatetodaymonth")[0], "click",
            function() {
                var a = [l.getFullYear(), l.getMonth() + 1, l.getDate()],
                g = b.parse([a[0], a[1], 0], [0, 0, 0], c.format);
                b.isValHtml(f) ? b.val(f, g) : b.text(f, g),
                b.html(e(d)[0], ""),
                b.shdeCell(!0),
                ("function" === c.choosefun || null != c.choosefun) && c.choosefun(g)
            })) : (b.each([h, i],
            function(d, e) {
                b.on(e, "click",
                function(d) {
                    var i, j, k;
                    b.stopmp(d),
                    i = parseInt(b.attr(m, "data-year")),
                    j = parseInt(b.attr(n, "data-month")),
                    e == h ? i -= 1 : i += 1,
                    k = l.toLocaleDateString() == i + "/" + j + "/" + l.getDate() ? g[2] : 1,
                    a.getDateStr(i, j, k),
                    a.clickLiDays(a, c, f)
                })
            }), b.each([j, k],
            function(d, e) {
                b.on(e, "click",
                function(d) {
                    var h, i, k;
                    b.stopmp(d),
                    h = parseInt(b.attr(m, "data-year")),
                    i = parseInt(b.attr(n, "data-month")),
                    e == j ? 1 == i ? (h -= 1, i = 12) : i -= 1 : 12 == i ? (h += 1, i = 1) : i += 1,
                    k = l.toLocaleDateString() == h + "/" + i + "/" + l.getDate() ? g[2] : 1,
                    a.getDateStr(h, i, k),
                    a.clickLiDays(a, c, f)
                })
            }), b.each(e(d + " .jedatebot .jedatehms em"),
            function(a, c) {
                b.on(c, "click",
                function() {
                    var g, l, f = "",
                    h = e(d + " .jedateprophms")[0],
                    i = b.attr(c, "data-hms"),
                    j = ["小时", "分钟", "秒数"],
                    k = function() {
                        b.removeClass(h, 24 == i ? "jedateh": "jedatems"),
                        b.html(h, "")
                    };
                    for (f += '<div class="jedatehmstitle">' + j[a] + '<div class="jedatehmsclose">&times;</div></div>', l = 0; i > l; l++) l = b.digit(l),
                    g = b.text(c) == l ? "action": "",
                    f += '<p class="' + g + '">' + l + "</p>";
                    b.removeClass(h, 24 == i ? "jedatems": "jedateh").addClass(h, 24 == i ? "jedateh": "jedatems"),
                    b.html(h, f),
                    b.each(e(d + " .jedateprophms p"),
                    function(a, d) {
                        b.on(d, "click",
                        function() {
                            b.html(c, b.digit(b.text(d))),
                            k()
                        })
                    }),
                    b.each(e(d + " .jedateprophms .jedatehmstitle"),
                    function(a, c) {
                        b.on(c, "click",
                        function() {
                            k()
                        })
                    })
                })
            }), b.on(e(d + " .jedatebot .jedatetodaymonth")[0], "click",
            function() {
                var g = [l.getFullYear(), l.getMonth() + 1, l.getDate(), l.getHours(), l.getMinutes(), l.getSeconds()],
                h = b.parse([g[0], g[1], g[2]], [g[3], g[4], g[5]], c.format);
                a.getDateStr(g[0], g[1], g[2]),
                b.isValHtml(f) ? b.val(f, h) : b.text(f, h),
                b.html(e(d)[0], ""),
                b.shdeCell(!0),
                ("function" === c.choosefun || null != c.choosefun) && c.choosefun(h)
            })),
            b.on(e(d + " .jedatebot .jedateclear")[0], "click",
            function() {
                var a = b.isValHtml(f) ? b.val(f) : b.text(f);
                b.isValHtml(f) ? b.val(f, "") : b.text(f, ""),
                b.html(e(d)[0], ""),
                b.shdeCell(!0),
                "" != a && ("function" === c.clearfun || null != c.clearfun) && c.clearfun(a)
            }),
            b.on(e(d + " .jedatebot .jedateok")[0], "click",
            function(h) {
                var i, j, k, l, p;
                b.stopmp(h),
                i = o ? e(d + " .jedaym li") : e(d + " .jedaul li"),
                o ? (p = "" != b.val(f) || "" != b.text(f) ? b.attr(e(d + " .jedaym .action")[0], "data-onym").match(/\w+|d+/g) : "", l = "" != b.val(f) || "" != b.text(f) ? b.parse([p[0], p[1], 1], [0, 0, 0], c.format) : "") : (j = [], k = [parseInt(b.attr(m, "data-year")), parseInt(b.attr(n, "data-month")), g[2]], b.each(e(d + " .jedatehms em"),
                function(a, c) {
                    j.push(b.text(c))
                }), l = "" != b.val(f) || "" != b.text(f) ? b.parse([k[0], k[1], k[2]], [j[0], j[1], j[2]], c.format) : "", a.getDateStr(k[0], k[1], k[2])),
                b.each(i,
                function(a, c) {
                    "action" == b.attr(c, "class") && (b.isValHtml(f) ? b.val(f, l) : b.text(f, l))
                }),
                b.html(e(d)[0], ""),
                b.shdeCell(!0),
                "" != l && ("function" === c.okfun || null != c.okfun) && c.okfun(l)
            }),
            b.on(document, "click",
            function() {
                b.shdeCell(!0),
                b.html(e(d)[0], "")
            }),
            b.on(e(d)[0], "click",
            function(a) {
                b.stopmp(a)
            }),
            a.clickLiDays(a, c, f)
        },
        YearAndMonth: function(a, c, f, g) {
            function o(a) {
                var c = "";
                return b.each(new Array(15),
                function(d) {
                    if (7 === d) {
                        var e = b.attr(k, "data-year");
                        c += "<li " + (e == a ? 'class="action"': "") + ' data-y="' + a + '">' + a + "年</li>"
                    } else c += '<li data-y="' + (a - 7 + d) + '">' + (a - 7 + d) + "年</li>"
                }),
                c
            }
            function p(a, c) {
                var f = "";
                12 == c ? (b.each(b.montharr,
                function(a, c) {
                    var d = b.attr(l, "data-month"),
                    c = b.digit(c);
                    f += "<li " + (d == c ? 'class="action"': "") + ' data-m="' + c + '">' + c + "月</li>"
                }), b.each([m, n],
                function(a, b) {
                    b.style.display = "none"
                })) : (f = o(a), b.each([m, n],
                function(a, b) {
                    b.style.display = "block"
                })),
                b.removeClass(h, 12 == c ? "jedatesety": "jedatesetm").addClass(h, 12 == c ? "jedatesetm": "jedatesety"),
                b.html(e(d + " .jedatetopym .ymdropul")[0], f),
                h.style.display = "block"
            }
            function q(i) {
                b.each(e(d + " .ymdropul li"),
                function(d, e) {
                    b.on(e, "click",
                    function() {
                        var e = b.attr(this, "data-y"),
                        j = b.attr(l, "data-month");
                        b.attr(i, "data-year", e),
                        b.html(i, e),
                        h.style.display = "none",
                        a.getDateStr(e, j, g[2]),
                        a.clickLiDays(a, c, f)
                    })
                })
            }
            var r, h = e(d + " .jedatetopym")[0],
            i = e(d + " .jedateyy")[0],
            j = e(d + " .jedatemm")[0],
            k = e(d + " .jedateyy .jedateyear")[0],
            l = e(d + " .jedatemm .jedatemonth")[0],
            m = e(d + " .jedateymchri")[0],
            n = e(d + " .jedateymchle")[0];
            b.on(i, "click",
            function() {
                var a = parseInt(b.attr(i, "data-ym")),
                c = parseInt(b.attr(k, "data-year"));
                p(c, a),
                q(k)
            }),
            b.on(j, "click",
            function() {
                var i = parseInt(b.attr(j, "data-ym")),
                m = parseInt(b.attr(k, "data-year"));
                p(m, i),
                b.each(e(d + " .ymdropul li"),
                function(d, e) {
                    b.on(e, "click",
                    function() {
                        var e = b.attr(k, "data-year"),
                        i = b.attr(this, "data-m");
                        b.attr(l, "data-month", i),
                        b.html(l, i),
                        h.style.display = "none",
                        a.getDateStr(e, i, g[2]),
                        a.clickLiDays(a, c, f)
                    })
                })
            }),
            b.on(e(d + " .jedateymchok")[0], "click",
            function(a) {
                b.stopmp(a),
                h.style.display = "none"
            }),
            r = parseInt(b.attr(k, "data-year")),
            b.each([n, m],
            function(a, c) {
                b.on(c, "click",
                function(c) {
                    b.stopmp(c),
                    0 == a ? r -= 15 : r += 15;
                    var f = o(r);
                    b.html(e(d + " .jedatetopym .ymdropul")[0], f),
                    q(k)
                })
            })
        },
        clickLiDays: function(a, c, f) {
            b.each(e(d + " .jedaul li"),
            function(g, h) {
                b.on(h, "click",
                function(g) {
                    var i, j, k, l, m;
                    b.hasClass(h, "disabled") || (b.stopmp(g), i = [], b.each(e(d + " .jedatehms em"),
                    function(a, c) {
                        i.push(b.text(c))
                    }), j = 0 | parseInt(b.attr(h, "data-y")), k = 0 | parseInt(b.attr(h, "data-m")), l = 0 | parseInt(b.attr(h, "data-d")), m = b.parse([j, k, l], [i[0], i[1], i[2]], c.format), a.getDateStr(j, k, l), b.isValHtml(f) ? b.val(f, m) : b.text(f, m), b.html(e(d)[0], ""), b.shdeCell(!0), ("function" === c.choosefun || null != c.choosefun) && c.choosefun(m))
                })
            })
        }
    },
    b.getPath = function() {
        var a = document.scripts,
        b = a[a.length - 1].src;
        return b.substring(0, b.lastIndexOf("/") + 1)
    } (),
    b.creatlink = function(a) {
        var c = document.createElement("link");
        c.type = "text/css",
        c.rel = "stylesheet",
        c.href = b.getPath + "skin/" + a + ".css",
        c.id = "jeDateSkin",
        e("head")[0].appendChild(c),
        c = null
    },
    b.creatlink("jedate"),
    h.skin = function(a) {
        e("#jeDateSkin")[0].parentNode.removeChild(e("#jeDateSkin")[0]),
        b.creatlink(a)
    },
    h.now = function(a) {
        var c, d, e, b = new Date;
        return b.setDate(b.getDate() + a),
        c = b.getFullYear(),
        d = b.getMonth() + 1,
        e = b.getDate(),
        c + "-" + d + "-" + e
    },
    "function" == typeof define ? define(function() {
        return h
    }) : window.jeDate = h
} (window);