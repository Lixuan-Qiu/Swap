
    <div class="panel-body">
    <table>
        <thead>
            <th>
            Items
            </th>
            <th>
            </th>
            <th>
            </th>
            <th>
            </th>
            <th>
            </th>
        </thead>
        <tbody>
            {{#each mData}}
            <tr>
            <td>
            <p id={{this.itemId}}class="itemList-scroll">{{this.itemDescription}}</p>
            </td>
            </tr>
            {{/each}}
        </tbody>
    </table>
    </div>
