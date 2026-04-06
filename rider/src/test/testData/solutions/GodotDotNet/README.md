# TCG Hand Layout
## English

A plugin to serve as a card game layout for the player's hand.

I've remade the plugin from cyanglaz repository using Godot .NET instead.

This plugin is designed for Godot .NET users who prefer not to mix C# and GDScript in their projects.

**Current State**: Added some more stuff over the original plugin, view the Release note to know more.

### Features:

- Control node for laying out cards in a hand layout.

- View hand layout directly in the editor. Any update on the variables reflect on the cards position in real time.

- Configurable hover animation.

- Node-free solution for providing layout information for a hand layout.

- Dragging cards (Beta).

### Some differences:

- This version is more object-oriented. This means that I've separated some functions from the core script into their own classes.

- I've cleansed some stuff that was probably for future updates on the original plugin such as some variables that weren't ever used.

- Added the option to allow the card, when hovered, to appear above other cards in the rendering or maintain its original ZIndex.

### Future updates

- ~~Add card activation logic, either by click (with confirmation) or by dragging and dropping to a region of the screen defined by the developer, allowing any function to be called for activation.~~

- ~~Make more fine adjustments to the layout to make it increasingly customizable, even though it’s already at an optimal point.~~

- Document the original code comments that are in English and translate them into Portuguese in a separate Readme file.

- Later updates will be on release notes.

## Português
 
Eu refiz o plugin do repositório do cyanglaz mas usando Godot .NET no lugar.

Esse plugin foi desenvolvido pra desenvolvedores do Godot .NET que não querem misturar scripts C# com GDScript nos seus projetos.

**Estado atual**: Adicionei mais umas coisas sobre o plugin original. Veja as notas de release para saber mais.

### Funcionalidades:

- Um node Control para mostrar as cartas num layout de mão de TCG.

- Pode ver o layout diretamente pelo editor.

- Qualquer alteração que fizer nas variáveis do editor mostra no layout em tempo real.

- Animação de hover configurável.

- Solução que não precisa de Nodes para prover informação de layouts para a mão.

- Possibilidade de arrastar as cartas (beta)

### Algumas diferenças:

- Essa versão está um pouco mais orientada a objeto. Isso significa que eu separei um pouco as classes do script principal em suas próprias classes.

- Limpei algumas coisas que, provavelmente, eram feitas para updates futuros do plugin original como, por exemplo, algumas variáveis que não estavam sendo usadas.

- Adicionei a opção de permitir que a carta, quando em hover, ficar por cima das outras cartas na renderização ou manter seu ZIndex original.

### Futuros updates

- ~~Adicionar lógica de ativação de carta, seja no clique (com confirmação) ou ao arrastar e jogar para uma região da tela definida pelo desenvolvedor, de forma que seja possível chamar qualquer função para realizar a ativação.~~ 
- ~~Criar mais ajustes finos no layout para ficar cada vez mais personalizável, apesar de já estar num ponto ótimo.~~

- Documentar os comentários originais do código que estão em inglês e deixá-los em português num Readme separado.

- Futuros updates agora ficam nas notas de releases.
