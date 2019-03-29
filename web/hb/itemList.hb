
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
            <p id={{mData.itemId}}class="itemList-scroll">{{mData.itemDescription}}</p>
            </td>
            </tr>
            {{/each}}
        </tbody>
    </table>
    </div>
