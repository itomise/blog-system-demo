---
title: "Next.js で CSS Modules / framer-motion でページ間遷移を作ったらうまくいかなかった話"
emoji: "📚"
type: "tech"
topics:
  - "nextjs"
  - "react"
published: true
published_at: "2020-11-21 15:20"
---

# Next.jsでのスタイリングどうするか問題
少し前にNext.jsの開発環境を整備しようかなといろいろスタイリングについても調べていて、さまざまな記事を参考にした結果 CSS Modulesを使うことにしました。

主な理由は以下です。
- styled-componentsは、タグの名前が変数になってしまってどれがstyled-componentsなのかわからなくなってしまうので×（同じ理由でほかのCSS in JSのライブラリも×）
- CSS in JSでもstyled-jsxは上記の問題がおきない感じだったので採用しようと思ったが、sassで設定したmixinやvariableなどが使えないのが微妙....（ほかのCSS in JSも一緒だと思うんですが）
- create-next-appなどでもデフォルトでCSS Modulesを使っていたので、これでいいかという感じで決定

# Next.jsでのPage Transition
画面遷移のアニメーションを作るとき、自分はいつもframer-motionを使っていました。
特段トラブルなども起きたことがなかったので、今回も使おうと思っていたのですが...
Nextのバージョンアップデートの影響なのか、画面遷移がうまくいきませんでした。（前まではうまくいってたはず...？）
以下バージョンです。
- next : 10.0.2
- react : 17.0.1
- react-dom : 17.0.1
- framer-motion : 2.9.4

## 挙動
localhostでSSRとして動いている時は問題なくアニメーションされるのですが、SSGされたあとの挙動がうまくいきませんでした。
ページ遷移した瞬間、遷移元のスタイルがすべて失われてしまって、一瞬cssが当たっていないような見た目になってしまいます。

gitはこちらです。
https://github.com/itomise/nextjs-with-framer-motion

うまくいってるとき（yarn dev）
![](https://storage.googleapis.com/zenn-user-upload/nzkph7qus745h0ev1dkmu855hisi)

うまくいってないとき（SSG）
![](https://storage.googleapis.com/zenn-user-upload/5zzmi2mcghbu9tdmd089ij5p53xd)

Next.js のexampleにある with-framer-motion というリポジトリの一部をCSS Modulesに書き換えて試しています。
トップの写真の並びや「Motion」という文字が、うまくいってないほうだと一瞬スタイルが切れているのがわかると思います。

## 原因てきな
CSS Modulesを使って`next export`すると、`__next/static/`配下にcssというフォルダができ、cssファイルが作られます。（CSS in JSの時は作られません）
画面遷移のタイミングでcssファイルが切れてしまうので、今回のような挙動になってしまうのかなと思いました。

## 対策
~~CSS in JS にすべて書き直すしかないのかな、と思います...~~

下記Discussionで@kkoudev さんにコメントいただきまして、解決しました。
ありがとうございます。

教えていただいた内容通り、

```
const routeChange = () => {
  const tempFix = () => {
    const allStyleElements = document.querySelectorAll('link')
    allStyleElements.forEach((elem) => {
      if (elem.as === 'style') {
        elem.rel = 'stylesheet'
      }
    })
  }
  tempFix()
}
Router.events.on("routeChangeComplete", routeChange );
Router.events.on("routeChangeStart", routeChange );
```

~~のような感じで linkタグで `as="style"` になっているタグの rel を stylesheet に変えると、ページ遷移でもリンクが切れないようになりました。~~

**追記**
上記コードでnext.js最新環境で動かしたところうまくいかなかったのですが、以下の方のコードを参考にすると正しく動くようになりました。
https://github.com/vercel/next.js/issues/17464#issuecomment-751267740

高速でルーティングを複数回行うと（？）cleanupでエラーを吐くので、cleanupのcopiesのcopyで`copy.parentNode`がtrueの時のみremoveChildをする、という処理をいれるとそのエラーも回避できるようです。